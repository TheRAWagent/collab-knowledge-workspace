package com.dj.ckw.userservice.service;

import com.dj.ckw.userservice.config.RedisPubSubConfig;
import com.dj.ckw.userservice.dto.CreateUserRequestDto;
import com.dj.ckw.userservice.dto.UpdateUserRequestDto;
import com.dj.ckw.userservice.dto.UserResponseDto;
import com.dj.ckw.userservice.exception.UserAlreadyExistsException;
import com.dj.ckw.userservice.exception.UserNotFoundException;
import com.dj.ckw.userservice.model.EmailVerifications;
import com.dj.ckw.userservice.model.RequestInfo;
import com.dj.ckw.userservice.model.User;
import com.dj.ckw.userservice.repository.EmailVerificationsRepository;
import com.dj.ckw.userservice.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final RequestInfo requestInfo;
  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;
  private final EmailVerificationsRepository emailVerificationsRepository;
  private final OtpService otpService;

  protected UserService() {
    this.userRepository = null;
    this.requestInfo = null;
    this.redisTemplate = null;
    this.objectMapper = null;
    this.emailVerificationsRepository = null;
    this.otpService = null;
  }

  public UserService(UserRepository userRepository, RequestInfo requestInfo,
      StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
      EmailVerificationsRepository emailVerificationsRepository, OtpService otpService) {
    this.userRepository = userRepository;
    this.requestInfo = requestInfo;
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
    this.emailVerificationsRepository = emailVerificationsRepository;
    this.otpService = otpService;
  }

  public UserResponseDto getUser(String email) {
    Optional<User> optionalUser = userRepository.findByEmail(email);

    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException(email);
    }

    User user = optionalUser.get();

    return new UserResponseDto(user.getName(), user.getEmail(), user.getAvatarUrl(), user.getVerified());
  }

  @Transactional
  public void createUser(CreateUserRequestDto createUserRequestDto) {
    log.info("Creating user: {}", createUserRequestDto.getEmail());
    Optional<User> userOptional = userRepository.findByEmail(createUserRequestDto.getEmail());

    if (userOptional.isPresent()) {
      throw new UserAlreadyExistsException(createUserRequestDto.getEmail());
    }

    User user = new User(createUserRequestDto.getEmail(), null);
    userRepository.save(user);

    publishEvent("EMAIL_VERIFICATION_REQUESTED", objectMapper.createObjectNode()
        .put("email", user.getEmail())
        .put("purpose", "SIGNUP"));

    log.info("User created successfully: {}", createUserRequestDto.getEmail());
  }

  public UserResponseDto updateUser(UpdateUserRequestDto updateUserRequestDto) {
    log.info("Updating user: {}", requestInfo.getEmail());
    Optional<User> optionalUser = userRepository.findByEmail(requestInfo.getEmail());

    if (optionalUser.isEmpty()) {
      throw new UserNotFoundException(requestInfo.getEmail());
    }

    User user = optionalUser.get();
    if (updateUserRequestDto.getName() != null) {
      user.setName(updateUserRequestDto.getName());
    }

    // More update fields to be added later

    userRepository.save(user);

    return new UserResponseDto(user.getName(), user.getEmail(), user.getAvatarUrl(), user.getVerified());
  }

  public void verifyEmail(String email, String code) {
    log.info("Verifying email: {}", email);
    Optional<EmailVerifications> verification = emailVerificationsRepository.findByEmail(email);

    if (verification.isEmpty()) {
      throw new UserNotFoundException(email);
    }

    EmailVerifications emailVerification = verification.get();

    if (emailVerification.getAttempts() >= emailVerification.getMaxAttempts()) {
      log.warn("Max verification attempts exceeded for email: {}", email);
      throw new IllegalArgumentException("Maximum verification attempts exceeded, please request a new code");
    }

    if (emailVerification.getExpiresAt().isBefore(LocalDateTime.now())) {
      log.warn("Verification code expired for email: {}", email);
      throw new IllegalArgumentException("Verification code has expired");
    }

    if (!otpService.matches(code, emailVerification.getOtpHash())) {
      emailVerification.setAttempts(emailVerification.getAttempts() + 1);
      log.warn("Invalid verification code for email: {}", email);
      throw new IllegalArgumentException("Invalid verification code");
    }

    Optional<User> user = userRepository.findByEmail(email);

    if (user.isEmpty()) {
      throw new UserNotFoundException(email);
    }

    User existingUser = user.get();

    existingUser.setVerified(true);

    userRepository.save(existingUser);
    log.info("Email verified successfully: {}", email);
  }

  @Transactional
  public void resendVerification(String email) {
    log.info("Resending verification for email: {}", email);
    Optional<User> userOptional = userRepository.findByEmail(email);

    if (userOptional.isEmpty()) {
      throw new UserNotFoundException(email);
    }

    publishEvent("RESEND_EMAIL_VERIFICATION_OTP_REQUESTED", objectMapper.createObjectNode()
        .put("email", email)
        .put("purpose", "SIGNUP"));
  }

  private void publishEvent(String eventType, ObjectNode payload) {
    try {
      ObjectNode event = objectMapper.createObjectNode();
      event.put("eventType", eventType);
      event.set("payload", payload);
      String jsonEvent = objectMapper.writeValueAsString(event);
      redisTemplate.convertAndSend(RedisPubSubConfig.USER_EVENTS_TOPIC, jsonEvent);
      log.info("Published event: {} to Redis", eventType);
    } catch (JsonProcessingException e) {
      log.error("Failed to publish event: {}", eventType, e);
    }
  }

  public void sendResetPasswordCode(String email) {
    log.info("Initiating password reset for email: {}", email);
    // Placeholder for generating and sending reset code
  }

  public void verifyResetPasswordCode(String email, String code) {
    log.info("Verifying reset code for email: {}", email);
    // Placeholder for verification logic
  }
}
