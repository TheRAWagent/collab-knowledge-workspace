ALTER TABLE document_snapshots
    DROP CONSTRAINT IF EXISTS document_snapshots_pkey;

ALTER TABLE document_snapshots
    ADD COLUMN id UUID DEFAULT uuidv7() NOT NULL;

ALTER TABLE document_snapshots
    ADD CONSTRAINT document_snapshots_pkey PRIMARY KEY (id);

CREATE INDEX idx_document_snapshots_document_id ON document_snapshots (document_id);
