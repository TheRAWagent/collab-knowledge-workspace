package com.dj.ckw.userservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VerifyUserRequestDto {
    @NotNull
    private String email;

    @NotNull
    @Size(min = 6, max = 6)
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public VerifyUserRequestDto(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
