package com.dj.ckw.userservice.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Table(name = "users")
public class User implements Persistable<UUID> {
  @Id
  private UUID id;

  @Column("email")
  @NotNull(message = "Email is required")
  @NotBlank(message = "Email cannot be empty")
  @Email
  private String email;

  @Column("name")
  @NotBlank(message = "Name cannot be empty")
  @NotNull(message = "Name is required")
  private String name;

  @Column("avatar_url")
  private String avatarUrl;

  @Column("created_at")
  @CreatedDate
  private Date createdAt;

  @Column("updated_at")
  @LastModifiedDate
  private Date updatedAt;

  @Column("verified")
  private boolean verified;

  public boolean getVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

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

  public UUID getId() {
    return id;
  }

  @Override
  public boolean isNew() {
    return this.id == null;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  /*
   * To be added later
   * preferences (jsonb)
   * is_onboarded
   * last_seen
   * phone
   * timezone
   * locale
   */
}
