package com.dj.ckw.userservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email cannot be empty")
    @Email
    private String email;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be empty")
    @NotNull(message = "Name is required")
    private String name;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /*
    To be added later
    preferences (jsonb)
    is_onboarded
    last_seen
    phone
    timezone
    locale
     */
}
