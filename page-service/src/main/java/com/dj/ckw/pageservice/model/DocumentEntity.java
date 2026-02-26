package com.dj.ckw.pageservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "documents")
public class DocumentEntity {
  @Id
  private UUID id;

  private UUID workspaceId;

  private String title;

  private String icon;

  private UUID directoryId;

  private int sortOrder;

  /**
   * Optimistic version
   */
  private int version;

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime updatedAt;

  public DocumentEntity() {
  }

  public DocumentEntity(UUID id, UUID workspaceId, String title, String icon, UUID directoryId, int sortOrder,
      int version, LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.workspaceId = workspaceId;
    this.title = title;
    this.icon = icon;
    this.directoryId = directoryId;
    this.sortOrder = sortOrder;
    this.version = version;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getWorkspaceId() {
    return workspaceId;
  }

  public void setWorkspaceId(UUID workspaceId) {
    this.workspaceId = workspaceId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public UUID getDirectoryId() {
    return directoryId;
  }

  public void setDirectoryId(UUID directoryId) {
    this.directoryId = directoryId;
  }

  public int getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public static DocumentEntityBuilder builder() {
    return new DocumentEntityBuilder();
  }

  public static class DocumentEntityBuilder {
    private UUID id;
    private UUID workspaceId;
    private String title;
    private String icon;
    private UUID directoryId;
    private int sortOrder;
    private int version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DocumentEntityBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public DocumentEntityBuilder workspaceId(UUID workspaceId) {
      this.workspaceId = workspaceId;
      return this;
    }

    public DocumentEntityBuilder title(String title) {
      this.title = title;
      return this;
    }

    public DocumentEntityBuilder icon(String icon) {
      this.icon = icon;
      return this;
    }

    public DocumentEntityBuilder directoryId(UUID directoryId) {
      this.directoryId = directoryId;
      return this;
    }

    public DocumentEntityBuilder sortOrder(int sortOrder) {
      this.sortOrder = sortOrder;
      return this;
    }

    public DocumentEntityBuilder version(int version) {
      this.version = version;
      return this;
    }

    public DocumentEntityBuilder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public DocumentEntityBuilder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public DocumentEntity build() {
      return new DocumentEntity(id, workspaceId, title, icon, directoryId, sortOrder, version, createdAt, updatedAt);
    }
  }
}
