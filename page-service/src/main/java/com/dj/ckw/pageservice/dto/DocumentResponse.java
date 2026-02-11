package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.model.DocumentEntity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class DocumentResponse {
    @NotNull
    private UUID id;

    @NotNull
    private UUID workspaceId;

    @NotNull
    private String title;

    @NotNull
    private String icon;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    public DocumentResponse() {
    }

    public DocumentResponse(UUID id, UUID workspaceId, String title, String icon, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.title = title;
        this.icon = icon;
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

    public static DocumentResponse create(DocumentEntity documentEntity) {
        return DocumentResponse.builder()
                .id(documentEntity.getId())
                .workspaceId(documentEntity.getWorkspaceId())
                .title(documentEntity.getTitle())
                .icon(documentEntity.getIcon())
                .createdAt(documentEntity.getCreatedAt())
                .updatedAt(documentEntity.getUpdatedAt())
                .build();
    }

    public static DocumentResponseBuilder builder() {
        return new DocumentResponseBuilder();
    }

    public static class DocumentResponseBuilder {
        private UUID id;
        private UUID workspaceId;
        private String title;
        private String icon;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public DocumentResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public DocumentResponseBuilder workspaceId(UUID workspaceId) {
            this.workspaceId = workspaceId;
            return this;
        }

        public DocumentResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        public DocumentResponseBuilder icon(String icon) {
            this.icon = icon;
            return this;
        }

        public DocumentResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DocumentResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DocumentResponse build() {
            return new DocumentResponse(id, workspaceId, title, icon, createdAt, updatedAt);
        }
    }
}
