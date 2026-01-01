package com.dj.ckw.pageservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record SnapshotResponse(
        Long version,
        Long schemaVersion,
        OffsetDateTime generatedAt,
        UUID updatedBy,
        String source,
        String contentJson
) {
}
