package com.dj.ckw.pageservice.model;

import io.r2dbc.postgresql.codec.Json;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table("document_snapshots")
@NoArgsConstructor
@Getter
@Setter
public class Snapshot {
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
}
