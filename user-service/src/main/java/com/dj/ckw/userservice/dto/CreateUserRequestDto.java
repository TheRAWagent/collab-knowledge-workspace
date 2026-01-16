package com.dj.ckw.userservice.dto;

import jakarta.validation.constraints.NotNull;

public class CreateUserRequestDto {
    @NotNull
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CreateUserRequestDto(String email) {
        this.email = email;
    }
}
