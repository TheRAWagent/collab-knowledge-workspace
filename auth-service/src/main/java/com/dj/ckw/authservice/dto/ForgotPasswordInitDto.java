package com.dj.ckw.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordInitDto {
  @NotBlank(message = "Email is required")
  @Email(message = "Email should be a valid email address")
  private String email;

  public ForgotPasswordInitDto() {
  }

  public ForgotPasswordInitDto(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
