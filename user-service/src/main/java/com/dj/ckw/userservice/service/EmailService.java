package com.dj.ckw.userservice.service;

public interface EmailService {
  void sendAccountVerificationOtpEmail(String toEmail, String otp);
}