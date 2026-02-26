package com.dj.ckw.userservice.service;

import com.dj.ckw.userservice.model.EmailVerifications;
import com.dj.ckw.userservice.repository.EmailVerificationsRepository;
import com.dj.ckw.userservice.repository.UserRepository;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserEventListener implements MessageListener {

  private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);
  private final ObjectMapper objectMapper;
  private final EmailService emailService;
  private final OtpService otpService;
  private final EmailVerificationsRepository emailVerificationsRepository;
  private final UserRepository userRepository;

  public UserEventListener(ObjectMapper objectMapper, EmailService emailService,
      OtpService otpService, EmailVerificationsRepository emailVerificationsRepository,
      UserRepository userRepository) {
    this.objectMapper = objectMapper;
    this.emailService = emailService;
    this.otpService = otpService;
    this.emailVerificationsRepository = emailVerificationsRepository;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public void onMessage(Message message, byte[] pattern) {
    try {
      String messageBody = new String(message.getBody());
      log.info("Received event: {}", messageBody);
      JsonNode root = objectMapper.readTree(messageBody);
      String eventType = root.get("eventType").asString();
      JsonNode payload = root.get("payload");

      switch (eventType) {
        case "EMAIL_VERIFICATION_REQUESTED":
          handleEmailVerificationRequested(payload);
          break;
        case "RESEND_EMAIL_VERIFICATION_OTP_REQUESTED":
          handleResendEmailVerificationOtpRequested(payload);
          break;
        case "PASSWORD_RESET_REQUESTED":
          handlePasswordResetRequested(payload);
          break;
        default:
          log.warn("Unknown event type: {}", eventType);
      }
    } catch (Exception e) {
      log.error("Error processing user event", e);
    }
  }

  private void handleEmailVerificationRequested(JsonNode payload) {
    String email = payload.get("email").asString();
    String otp = generateOtp();
    String hashedOtp = otpService.hash(otp);

    EmailVerifications emailVerification = new EmailVerifications();
    emailVerification.setEmail(email);
    emailVerification.setOtpHash(hashedOtp);
    emailVerification.setMaxAttempts(5);
    emailVerification.setPurpose("SIGNUP");
    emailVerification.setAttempts(0);
    emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(15));

    emailVerificationsRepository.save(emailVerification);
    emailService.sendAccountVerificationOtpEmail(email, otp);
    log.info("Email verification OTP sent to: {}", email);
  }

  private void handleResendEmailVerificationOtpRequested(JsonNode payload) {
    String email = payload.get("email").asString();
    Optional<EmailVerifications> existingVerificationOpt = emailVerificationsRepository.findByEmail(email);

    if (existingVerificationOpt.isPresent()) {
      String otp = generateOtp();
      String hashedOtp = otpService.hash(otp);
      EmailVerifications emailVerification = existingVerificationOpt.get();
      emailVerification.setOtpHash(hashedOtp);
      emailVerification.setAttempts(0);
      emailVerification.setExpiresAt(LocalDateTime.now().plusMinutes(15));
      emailVerificationsRepository.save(emailVerification);
      emailService.sendAccountVerificationOtpEmail(email, otp);
      log.info("Resent email verification OTP to: {}", email);
    } else {
      log.warn("Resend requested for non-existent verification: {}", email);
    }
  }

  /**
   * Handles PASSWORD_RESET_REQUESTED events published by auth-service.
   *
   * <p>
   * auth-service already generated the OTP and stored its BCrypt hash in Redis.
   * user-service is only responsible for delivering it to the user's inbox — but
   * only if the email belongs to a registered user. Unknown emails are silently
   * ignored to prevent user enumeration.
   */
  private void handlePasswordResetRequested(JsonNode payload) {
    String email = payload.get("email").asString();
    String otp = payload.get("otp").asString();

    boolean userExists = userRepository.findByEmail(email).isPresent();
    if (!userExists) {
      log.info("Password reset requested for unknown email (silently ignored): {}", email);
      return;
    }

    emailService.sendPasswordResetOtpEmail(email, otp);
    log.info("Password reset OTP email delivered to: {}", email);
  }

  private String generateOtp() {
    return String.format("%06d", java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 1_000_000));
  }
}
