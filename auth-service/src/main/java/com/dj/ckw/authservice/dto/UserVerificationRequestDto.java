package com.dj.ckw.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserVerificationRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address")
    private String email;
    @Size(min = 6, max = 6, message = "Otp must be 6 characters long")
    @Pattern(regexp = "^(?=.*\\d)\\d{6}$", message = "Otp must have 6 numbers")
    private String verificationCode;

    public String getEmail() {
        return email;
    }
}
