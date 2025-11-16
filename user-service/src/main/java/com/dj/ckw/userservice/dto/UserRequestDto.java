package com.dj.ckw.userservice.dto;

import com.dj.ckw.userservice.dto.validation.CreateUserValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequestDto {
    @NotNull(message = "name is required", groups = {CreateUserValidationGroup.class})
    @NotBlank(message = "Name cannot be empty")
    private String name;

    public UserRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
