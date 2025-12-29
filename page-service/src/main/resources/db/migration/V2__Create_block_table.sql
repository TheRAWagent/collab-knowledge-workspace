CREATE TABLE block
(
    id          CHAR(36) DEFAULT uuidv7() NOT NULL,
    document_id CHAR(36)                  NOT NULL,
    parent_id   CHAR(36),
    type        TEXT                      NOT NULL,
    position    TEXT                      NOT NULL,
    content     JSONB                     NOT NULL,
    attrs       JSONB,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()    NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW()    NOT NULL,
    CONSTRAINT block_pkey PRIMARY KEY (id)
);

ALTER TABLE block
    ADD CONSTRAINT block_document_id_fkey FOREIGN KEY (document_id) REFERENCES document (id) ON DELETE CASCADE;

ALTER TABLE block
    ADD CONSTRAINT block_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES block (id) ON DELETE CASCADE;