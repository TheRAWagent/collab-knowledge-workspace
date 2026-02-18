package com.dj.ckw.authservice.service;

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
import com.dj.ckw.authservice.config.NoOpUserIdentityStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.jspecify.annotations.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserRepository userRepository;
  private final UserIdentityConfirmationServiceGrpc.UserIdentityConfirmationServiceBlockingV2Stub userIdentityConfirmationServiceStub;
  private final NoOpUserIdentityStub fallbackStub;
  private final Logger log = LoggerFactory.getLogger(AuthService.class);

  protected AuthService() {
    this.userService = null;
    this.passwordEncoder = null;
    this.jwtUtil = null;
    this.userRepository = null;
    this.userIdentityConfirmationServiceStub = null;
    this.fallbackStub = null;
  }

  public AuthService(
      UserService userService,
      PasswordEncoder passwordEncoder,
      JwtUtil jwtUtil,
      UserRepository userRepository,
      UserIdentityConfirmationServiceGrpc.@Nullable UserIdentityConfirmationServiceBlockingV2Stub stub) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
    this.userRepository = userRepository;
    this.userIdentityConfirmationServiceStub = stub;
    this.fallbackStub = new NoOpUserIdentityStub();
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
      // Confirm user identity using real stub if available, otherwise use fallback
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
      // If gRPC call fails, use fallback
      log.warn("gRPC call failed, using fallback response: {}", e.getMessage());
      return fallbackStub.confirmUserIdentity(
          UserIdentityConfirmationRequest.newBuilder().setEmail(email).build());
    }
  }

  public void initiatePasswordReset(String email) {
    log.info("Initiating password reset for user: {}", email);
    // Implementation to be added later (will call User Service via gRPC)
  }

  public void resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
    log.info("Attempting to reset password for user: {}", resetPasswordRequestDto.getEmail());
    // Implementation:
    // 1. Call User Service via gRPC to verify the code
    // 2. If successful, hash newPassword and update in local DB
  }

  @CacheEvict(value = "users", key = "#token")
  public void logout(String token) {

  }
}
