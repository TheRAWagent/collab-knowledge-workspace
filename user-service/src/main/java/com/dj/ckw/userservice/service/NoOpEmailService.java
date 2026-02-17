package com.dj.ckw.userservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NoOpEmailService implements EmailService {
  private static Logger logger = LoggerFactory.getLogger(EmailService.class);

  @Override
  public void sendAccountVerificationOtpEmail(String toEmail, String otp) {
    logger.info("No-op email service: {}", toEmail);
  }
}
