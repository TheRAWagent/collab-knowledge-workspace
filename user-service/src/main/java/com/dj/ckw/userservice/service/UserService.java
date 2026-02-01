package com.dj.ckw.userservice.service;

import com.dj.ckw.userservice.dto.CreateUserRequestDto;
import com.dj.ckw.userservice.dto.UpdateUserRequestDto;
import com.dj.ckw.userservice.dto.UserResponseDto;
import com.dj.ckw.userservice.exception.UserAlreadyExistsException;
import com.dj.ckw.userservice.exception.UserNotFoundException;
import com.dj.ckw.userservice.model.DomainEvents;
import com.dj.ckw.userservice.model.EmailVerifications;
import com.dj.ckw.userservice.model.RequestInfo;
import com.dj.ckw.userservice.model.User;
import com.dj.ckw.userservice.repository.DomainEventsRepository;
import com.dj.ckw.userservice.repository.EmailVerificationsRepository;
import com.dj.ckw.userservice.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
  private static final Logger log = LoggerFactory.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final RequestInfo requestInfo;
  private final DomainEventsRepository domainEventsRepository;
  private final ObjectMapper objectMapper;
  private final EmailVerificationsRepository emailVerificationsRepository;
  private final OtpService otpService;

  public UserService(UserRepository userRepository, RequestInfo requestInfo,
      DomainEventsRepository domainEventsRepository, ObjectMapper objectMapper,
      EmailVerificationsRepository emailVerificationsRepository, OtpService otpService) {
    this.userRepository = userRepository;
    this.requestInfo = requestInfo;
    this.domainEventsRepository = domainEventsRepository;
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
    ObjectNode payload = objectMapper.createObjectNode();
    payload.set("email", objectMapper.convertValue(user.getEmail(), JsonNode.class));
    payload.set("purpose", objectMapper.convertValue("SIGNUP", JsonNode.class));

    DomainEvents event = new DomainEvents();
    event.setEventType("EMAIL_VERIFICATION_REQUESTED");
    event.setPayload(payload);
    event.setEventStatus("PENDING");
    domainEventsRepository.save(event);

    userRepository.save(user);
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

    ObjectNode payload = objectMapper.createObjectNode();
    payload.set("email", objectMapper.convertValue(email, JsonNode.class));
    payload.set("purpose", objectMapper.convertValue("SIGNUP", JsonNode.class));

    DomainEvents event = new DomainEvents();
    event.setEventType("RESEND_EMAIL_VERIFICATION_OTP_REQUESTED");
    event.setPayload(payload);
    event.setEventStatus("PENDING");
    domainEventsRepository.save(event);
  }
}
