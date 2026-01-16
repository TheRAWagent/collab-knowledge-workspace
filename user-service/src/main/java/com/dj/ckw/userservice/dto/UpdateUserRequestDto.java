package com.dj.ckw.userservice.dto;

public class UpdateUserRequestDto {
    private String name;

    public UpdateUserRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
