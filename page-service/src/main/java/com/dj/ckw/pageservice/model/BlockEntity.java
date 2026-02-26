package com.dj.ckw.pageservice.model;

import tools.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "blocks")
public class BlockEntity {
    @Id
    private UUID id;

    @Column("document_id")
    @NotNull
    private UUID documentId;

    @Column("parent_id")
    private UUID parentId;

    @Column("order_index")
    private Long orderIndex;

    @Column("type")
    private String type;

    private JsonNode attrs;

    @Column("text")
    private String text;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public BlockEntity() {
    }

    public BlockEntity(UUID id, UUID documentId, UUID parentId, Long orderIndex, String type, JsonNode attrs, String text, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.documentId = documentId;
        this.parentId = parentId;
        this.orderIndex = orderIndex;
        this.type = type;
        this.attrs = attrs;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public Long getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Long orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getAttrs() {
        return attrs;
    }

    public void setAttrs(JsonNode attrs) {
        this.attrs = attrs;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public static BlockEntityBuilder builder() {
        return new BlockEntityBuilder();
    }

    public static class BlockEntityBuilder {
        private UUID id;
        private UUID documentId;
        private UUID parentId;
        private Long orderIndex;
        private String type;
        private JsonNode attrs;
        private String text;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public BlockEntityBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public BlockEntityBuilder documentId(UUID documentId) {
            this.documentId = documentId;
            return this;
        }

        public BlockEntityBuilder parentId(UUID parentId) {
            this.parentId = parentId;
            return this;
        }

        public BlockEntityBuilder orderIndex(Long orderIndex) {
            this.orderIndex = orderIndex;
            return this;
        }

        public BlockEntityBuilder type(String type) {
            this.type = type;
            return this;
        }

        public BlockEntityBuilder attrs(JsonNode attrs) {
            this.attrs = attrs;
            return this;
        }

        public BlockEntityBuilder text(String text) {
            this.text = text;
            return this;
        }

        public BlockEntityBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public BlockEntityBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public BlockEntity build() {
            return new BlockEntity(id, documentId, parentId, orderIndex, type, attrs, text, createdAt, updatedAt);
        }
    }
}
