package com.dj.ckw.pageservice.model;

import io.r2dbc.postgresql.codec.Json;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("document_snapshots")
public class SnapshotEntity {
    @Id
    private UUID id;

    @Column("document_id")
    private UUID documentId;

    @Column("version")
    private Long version;

    @Column("schema_version")
    private Long schemaVersion;

    @Column("generated_at")
    private OffsetDateTime generatedAt;

    @Column("updated_by")
    private UUID updatedBy;

    @Column("source")
    private String source;

    @Column("content_json")
    private Json contentJson;

    public SnapshotEntity() {
    }

    public SnapshotEntity(UUID id, UUID documentId, Long version, Long schemaVersion, OffsetDateTime generatedAt, UUID updatedBy, String source, Json contentJson) {
        this.id = id;
        this.documentId = documentId;
        this.version = version;
        this.schemaVersion = schemaVersion;
        this.generatedAt = generatedAt;
        this.updatedBy = updatedBy;
        this.source = source;
        this.contentJson = contentJson;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(Long schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(OffsetDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Json getContentJson() {
        return contentJson;
    }

    public void setContentJson(Json contentJson) {
        this.contentJson = contentJson;
    }
}
