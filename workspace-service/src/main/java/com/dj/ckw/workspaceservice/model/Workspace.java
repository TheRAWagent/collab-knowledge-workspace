package com.dj.ckw.workspaceservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workspace")
@EntityListeners(AuditingEntityListener.class)
public class Workspace {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "owner_id", nullable = false)
  private String ownerId;

  @Column(name = "name", nullable = false)
  @NotBlank(message = "Name cannot be blank")
  @NotNull(message = "Name is required")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "created_at")
  @CreatedDate
  private Date createdAt;

  @Column(name = "updated_at")
  @UpdateTimestamp
  private Date updatedAt;

  @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WorkspaceMember> members = new ArrayList<>();

  public Workspace() {
  }

  public Workspace(UUID id, String ownerId, String name, String description, Date createdAt, Date updatedAt, List<WorkspaceMember> members) {
    this.id = id;
    this.ownerId = ownerId;
    this.name = name;
    this.description = description;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.members = members != null ? members : new ArrayList<>();
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public List<WorkspaceMember> getMembers() {
    return members;
  }

  public void setMembers(List<WorkspaceMember> members) {
    this.members = members;
  }

  public static WorkspaceBuilder builder() {
    return new WorkspaceBuilder();
  }

  public static class WorkspaceBuilder {
    private UUID id;
    private String ownerId;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private List<WorkspaceMember> members = new ArrayList<>();

    public WorkspaceBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public WorkspaceBuilder ownerId(String ownerId) {
      this.ownerId = ownerId;
      return this;
    }

    public WorkspaceBuilder name(String name) {
      this.name = name;
      return this;
    }

    public WorkspaceBuilder description(String description) {
      this.description = description;
      return this;
    }

    public WorkspaceBuilder createdAt(Date createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public WorkspaceBuilder updatedAt(Date updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public WorkspaceBuilder members(List<WorkspaceMember> members) {
      this.members = members != null ? members : new ArrayList<>();
      return this;
    }

    public Workspace build() {
      return new Workspace(id, ownerId, name, description, createdAt, updatedAt, members);
    }
  }
}
