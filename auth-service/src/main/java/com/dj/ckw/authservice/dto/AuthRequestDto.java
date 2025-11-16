package com.dj.ckw.authservice.dto;

import com.dj.ckw.authservice.dto.validators.CreateUserValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AuthRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 1 lowercase letter, 1 uppercase letter, 1 number, 1 special character, and be at least 8 characters long",
            groups = CreateUserValidationGroup.class
    )
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
