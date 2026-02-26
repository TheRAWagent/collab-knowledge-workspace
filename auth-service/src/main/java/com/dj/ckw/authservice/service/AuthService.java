package com.dj.ckw.authservice.service;

import com.dj.ckw.authservice.config.RedisPubSubConfig;
import com.dj.ckw.authservice.dto.AuthRequestDto;
import com.dj.ckw.authservice.dto.ResetPasswordRequestDto;
import com.dj.ckw.authservice.exception.UserAlreadyExistsException;
import com.dj.ckw.authservice.exception.UserNotFoundException;
import com.dj.ckw.authservice.grpc.UserIdentityConfirmationRequest;
import com.dj.ckw.authservice.grpc.UserIdentityConfirmationResponse;
import com.dj.ckw.authservice.grpc.UserIdentityConfirmationServiceGrpc;
import com.dj.ckw.authservice.model.User;
import com.dj.ckw.authservice.repository.UserRepository;
import com.dj.ckw.authservice.util.JwtUtil;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;
import tools.jackson.databind.node.ObjectNode;

import com.dj.ckw.authservice.config.NoOpUserIdentityStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;

@Service
public class AuthService {
  private static final int OTP_LENGTH = 6;
  private static final Duration OTP_TTL = Duration.ofMinutes(15);

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final UserIdentityConfirmationServiceGrpc.UserIdentityConfirmationServiceBlockingV2Stub userIdentityConfirmationServiceStub;
  private final NoOpUserIdentityStub fallbackStub;
  private final @Nullable StringRedisTemplate redisTemplate;
  private final @Nullable ObjectMapper objectMapper;
  private final Logger log = LoggerFactory.getLogger(AuthService.class);

  public AuthService(
      UserService userService,
      PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil,
      UserRepository userRepository,
      UserIdentityConfirmationServiceGrpc.@Nullable UserIdentityConfirmationServiceBlockingV2Stub stub,
      @Nullable StringRedisTemplate redisTemplate,
      @Nullable ObjectMapper objectMapper) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
    this.userIdentityConfirmationServiceStub = stub;
    this.fallbackStub = new NoOpUserIdentityStub();
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  public Optional<String> authenticate(AuthRequestDto authRequestDto) {
    log.info("Authenticating user: {}", authRequestDto.getEmail());
    return userService.findByEmail(authRequestDto.getEmail())
        .filter(user -> {
          boolean match = passwordEncoder.matches(authRequestDto.getPassword(), user.getPassword());
          if (!match) {
            log.warn("Invalid password for user: {}", authRequestDto.getEmail());
          }
          return match;
        })
        .map(user -> {
          log.info("User authenticated successfully: {}", authRequestDto.getEmail());
          return jwtUtil.generateToken(user.getEmail());
        });
  }

  @Cacheable(value = "users", key = "#token")
  public String validateToken(String token) {
    return jwtUtil.validateToken(token);
  }

  public void createUser(AuthRequestDto authRequestDto) {
    log.info("Attempting to create user: {}", authRequestDto.getEmail());
    if (userRepository.findByEmail(authRequestDto.getEmail()).isPresent()) {
      throw new UserAlreadyExistsException("User with email " + authRequestDto.getEmail() + " already exists");
    }

    try {
      UserIdentityConfirmationResponse response = confirmUserIdentity(authRequestDto.getEmail());

      if (response.getIsConfirmed()) {
        User user = new User();
        user.setEmail(authRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(authRequestDto.getPassword()));
        userRepository.save(user);
        log.info("User created successfully: {}", authRequestDto.getEmail());
      } else {
        log.warn("User identity not confirmed by User Service (may be in degraded mode): {}",
            authRequestDto.getEmail());
        throw new UserNotFoundException("User with email " + authRequestDto.getEmail()
            + " not found in User Service. Please ensure user-service is available.");
      }
    } catch (UserNotFoundException e) {
      throw e;
    } catch (Exception e) {
      log.error("Failed to create user: {}. Error: {}", authRequestDto.getEmail(), e.getMessage());
      throw new RuntimeException("Failed to create user: " + e.getMessage());
    }
  }

  /**
   * Initiates a password reset request using Redis Pub/Sub.
   *
   * <p>
   * A secure 6-digit OTP is generated and its BCrypt hash is stored in Redis
   * with a 15-minute TTL under the key {@code password_reset:{email}}.
   * The raw OTP is published along with the email on the {@code user-events}
   * topic
   * so the user-service can send it via email.
   *
   * <p>
   * Always returns without error regardless of whether the email exists,
   * to prevent user enumeration.
   */
  public void initiatePasswordReset(String email) {
    log.info("Initiating password reset for: {}", email);

    if (redisTemplate == null || objectMapper == null) {
      log.warn("Redis not available — password reset initiation skipped for: {}", email);
      return;
    }

    String otp = generateOtp();
    String hashedOtp = passwordEncoder.encode(otp);
    String redisKey = RedisPubSubConfig.PASSWORD_RESET_KEY_PREFIX + email;

    redisTemplate.opsForValue().set(redisKey, hashedOtp, OTP_TTL);
    log.debug("Stored password reset OTP hash in Redis for: {}", email);

    publishEvent("PASSWORD_RESET_REQUESTED", objectMapper.createObjectNode()
        .put("email", email)
        .put("otp", otp));
  }

  /**
   * Completes the password reset flow.
   *
   * <p>
   * Retrieves the hashed OTP from Redis, verifies the supplied code,
   * then updates the password in the local users table and removes the Redis key.
   * If the code is wrong, expired, or the key doesn't exist, the method returns
   * silently — callers always receive 200 to prevent oracle attacks.
   */
  public void resetPassword(ResetPasswordRequestDto dto) {
    log.info("Attempting password reset for: {}", dto.getEmail());

    if (redisTemplate == null) {
      log.warn("Redis not available — password reset skipped for: {}", dto.getEmail());
      return;
    }

    String redisKey = RedisPubSubConfig.PASSWORD_RESET_KEY_PREFIX + dto.getEmail();
    String storedHash = redisTemplate.opsForValue().get(redisKey);

    if (storedHash == null) {
      log.warn("No active reset session found in Redis for: {} (expired or never initiated)", dto.getEmail());
      return;
    }

    if (!passwordEncoder.matches(dto.getCode(), storedHash)) {
      log.warn("Invalid reset code supplied for: {}", dto.getEmail());
      return;
    }

    Optional<User> userOptional = userRepository.findByEmail(dto.getEmail());
    if (userOptional.isEmpty()) {
      log.warn("User not found in auth DB during reset for: {}", dto.getEmail());
      redisTemplate.delete(redisKey);
      return;
    }

    User user = userOptional.get();
    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    userRepository.save(user);

    redisTemplate.delete(redisKey);
    log.info("Password reset successfully for: {}", dto.getEmail());
  }

  private void publishEvent(String eventType, ObjectNode payload) {
    if (redisTemplate == null || objectMapper == null) {
      log.warn("Redis not available — event {} not published", eventType);
      return;
    }
    try {
      ObjectNode event = objectMapper.createObjectNode();
      event.put("eventType", eventType);
      event.set("payload", payload);
      String json = objectMapper.writeValueAsString(event);
      redisTemplate.convertAndSend(RedisPubSubConfig.USER_EVENTS_TOPIC, json);
      log.info("Published event: {} to Redis topic: {}", eventType, RedisPubSubConfig.USER_EVENTS_TOPIC);
    } catch (JsonNodeException e) {
      log.error("Failed to serialize event: {}", eventType, e);
    }
  }

  private String generateOtp() {
    return String.format("%0" + OTP_LENGTH + "d",
        new SecureRandom().nextInt((int) Math.pow(10, OTP_LENGTH)));
  }

  /**
   * Confirms user identity using real gRPC stub if available,
   * otherwise falls back to no-op implementation for degraded mode.
   */
  private UserIdentityConfirmationResponse confirmUserIdentity(String email) {
    try {
      if (userIdentityConfirmationServiceStub != null) {
        return userIdentityConfirmationServiceStub.confirmUserIdentity(
            UserIdentityConfirmationRequest.newBuilder().setEmail(email).build());
      } else {
        return fallbackStub.confirmUserIdentity(
            UserIdentityConfirmationRequest.newBuilder().setEmail(email).build());
      }
    } catch (Exception e) {
      log.warn("gRPC call failed, using fallback response: {}", e.getMessage());
      return fallbackStub.confirmUserIdentity(
          UserIdentityConfirmationRequest.newBuilder().setEmail(email).build());
    }
  }

  @CacheEvict(value = "users", key = "#token")
  public void logout(String token) {
  }
}
