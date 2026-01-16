package com.dj.ckw.userservice.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OtpService {
    private final Argon2PasswordEncoder encoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public String hash(String otp) {
        return encoder.encode(otp);
    }

    public boolean matches(String rawOtp, String hash) {
        return encoder.matches(rawOtp, hash);
    }
}
