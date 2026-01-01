package com.dj.ckw.pageservice.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class BlockSnapshotRequest {
    private int version;
    private int schemaVersion;
    private OffsetDateTime generatedAt;
    private UUID updatedBy;
    private String source;
    private BlockDto content;
}
