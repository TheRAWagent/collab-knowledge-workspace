package com.dj.ckw.pageservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public class BlockSnapshotRequest {
    private int version;
    private int schemaVersion;
    private OffsetDateTime generatedAt;
    private UUID updatedBy;
    private String source;
    private BlockDto content;

    public BlockSnapshotRequest() {
    }

    public BlockSnapshotRequest(int version, int schemaVersion, OffsetDateTime generatedAt, UUID updatedBy, String source, BlockDto content) {
        this.version = version;
        this.schemaVersion = schemaVersion;
        this.generatedAt = generatedAt;
        this.updatedBy = updatedBy;
        this.source = source;
        this.content = content;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
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

    public BlockDto getContent() {
        return content;
    }

    public void setContent(BlockDto content) {
        this.content = content;
    }
}
