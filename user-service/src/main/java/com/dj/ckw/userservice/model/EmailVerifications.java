package com.dj.ckw.userservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "email_verifications")
public class EmailVerifications {
    @Id
    private UUID id;

    private String email;

    private String purpose;

    @Column("verification_code_hash")
    private String verificationCodeHash;

    @Column("expires_at")
    private LocalDateTime expiresAt;

    private Integer attempts;

    @Column("max_attempts")
    private Integer maxAttempts;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtpHash() {
        return verificationCodeHash;
    }

    public void setOtpHash(String verificationCodeHash) {
        this.verificationCodeHash = verificationCodeHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expirationTime) {
        this.expiresAt = expirationTime;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
}
