package com.dj.ckw.workspaceservice.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public class WorkspaceResponse {
    @NotNull
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String ownerId;
    @NotNull
    private Date createdAt;
    @NotNull
    private Date updatedAt;

    public WorkspaceResponse() {
    }

    public WorkspaceResponse(UUID id, String name, String description, String ownerId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public static WorkspaceResponseBuilder builder() {
        return new WorkspaceResponseBuilder();
    }

    public static class WorkspaceResponseBuilder {
        private UUID id;
        private String name;
        private String description;
        private String ownerId;
        private Date createdAt;
        private Date updatedAt;

        public WorkspaceResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public WorkspaceResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public WorkspaceResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public WorkspaceResponseBuilder ownerId(String ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public WorkspaceResponseBuilder createdAt(Date createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public WorkspaceResponseBuilder updatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public WorkspaceResponse build() {
            return new WorkspaceResponse(id, name, description, ownerId, createdAt, updatedAt);
        }
    }
}

