package com.dj.ckw.userservice.service;

import com.dj.ckw.userservice.model.DomainEvents;
import com.dj.ckw.userservice.model.EmailVerifications;
import com.dj.ckw.userservice.repository.DomainEventsRepository;
import com.dj.ckw.userservice.repository.EmailVerificationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class Scheduler {

    private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final EmailService emailService;
    private final DomainEventsRepository domainEventsRepository;
    private final OtpService otpService;
    private final EmailVerificationsRepository emailVerificationsRepository;

    public Scheduler(EmailService emailService, DomainEventsRepository domainEventsRepository, OtpService otpService, EmailVerificationsRepository emailVerificationsRepository) {
        this.emailService = emailService;
        this.domainEventsRepository = domainEventsRepository;
        this.otpService = otpService;
        this.emailVerificationsRepository = emailVerificationsRepository;
    }

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void sendEmailVerificationOtp() {
        Optional<DomainEvents> eventOptional = domainEventsRepository.findFirstByEventTypeAndEventStatus("EMAIL_VERIFICATION_REQUESTED", "PENDING");

        if (eventOptional.isEmpty()) {
            return;
        }

        DomainEvents event = eventOptional.get();

        log.info("Processing event ID: {}", event.getId());

        // This check prevents processing the same event multiple times
        event.setEventStatus("PROCESSING");
        domainEventsRepository.save(event);

        String otp = String.format("%06d", java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 1_000_000));
        String hashedOtp = otpService.hash(otp);
        log.info("Generated OTP: {} for event ID: {}", otp, event.getId());

        EmailVerifications emailVerification = new EmailVerifications();
        emailVerification.setEmail(event.getPayload().get("email").asText());
        emailVerification.setOtpHash(hashedOtp);
        emailVerification.setMaxAttempts(5);
        emailVerification.setPurpose("SIGNUP");
        emailVerification.setAttempts(0);
        emailVerification.setExpiresAt(java.time.LocalDateTime.now().plusMinutes(15));

        emailVerificationsRepository.save(emailVerification);

        emailService.sendAccountVerificationOtpEmail(emailVerification.getEmail(), otp);

        event.setEventStatus("COMPLETED");
        domainEventsRepository.save(event);
    }

    @Scheduled(fixedRate = 2000)
    @Transactional
    public void resendEmailVerificationOtp() {
        Optional<DomainEvents> eventOptional = domainEventsRepository.findFirstByEventTypeAndEventStatus("RESEND_EMAIL_VERIFICATION_OTP_REQUESTED", "PENDING");

        if (eventOptional.isEmpty()) {
            return;
        }

        DomainEvents event = eventOptional.get();

        log.info("Processing event ID: {}", event.getId());

        // This check prevents processing the same event multiple times
        event.setEventStatus("PROCESSING");
        domainEventsRepository.save(event);

        String otp = String.format("%06d", java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 1_000_000));
        String hashedOtp = otpService.hash(otp);
        log.info("Generated OTP: {} for event ID: {}", otp, event.getId());

        Optional<EmailVerifications> existingVerificationOpt = emailVerificationsRepository.findByEmail(event.getPayload().get("email").asText());
        EmailVerifications emailVerification;

        if (existingVerificationOpt.isPresent()) {
            emailVerification = existingVerificationOpt.get();
            emailVerification.setOtpHash(hashedOtp);
            emailVerification.setAttempts(0);
            emailVerification.setExpiresAt(java.time.LocalDateTime.now().plusMinutes(15));
            emailVerificationsRepository.save(emailVerification);
            emailService.sendAccountVerificationOtpEmail(emailVerification.getEmail(), otp);
            event.setEventStatus("COMPLETED");
        } else {
            event.setEventStatus("FAILED");
        }

        domainEventsRepository.save(event);
    }
}
