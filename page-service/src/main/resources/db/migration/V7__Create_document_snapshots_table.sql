CREATE TABLE document_snapshots
(
    document_id    UUID        NOT NULL PRIMARY KEY,
    version        BIGINT      NOT NULL,
    content_json   JSONB       NOT NULL,
    updated_by     UUID        NOT NULL,
    generated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    source         TEXT        NOT NULL,
    schema_version BIGINT      NOT NULL DEFAULT 1
)