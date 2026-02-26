package com.dj.ckw.pageservice.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "directories")
public class DirectoryEntity {

  @Id
  private UUID id;

  private UUID workspaceId;

  private UUID parentId;

  private String name;

  private int sortOrder;

  @CreatedDate
  private LocalDateTime createdAt;

  public DirectoryEntity() {
  }

  public DirectoryEntity(UUID id, UUID workspaceId, UUID parentId, String name, int sortOrder,
      LocalDateTime createdAt) {
    this.id = id;
    this.workspaceId = workspaceId;
    this.parentId = parentId;
    this.name = name;
    this.sortOrder = sortOrder;
    this.createdAt = createdAt;
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

  public UUID getParentId() {
    return parentId;
  }

  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(int sortOrder) {
    this.sortOrder = sortOrder;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
