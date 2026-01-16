package com.dj.ckw.userservice.dto;

public class UserResponseDto {
    private String name;
    private String email;
    private String avatarUrl;
    private Boolean verified;

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public UserResponseDto(String name, String email, String avatarUrl, Boolean verified) {
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
