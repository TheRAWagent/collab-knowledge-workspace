CREATE TABLE document
(
    id           CHAR(36) DEFAULT uuidv7() NOT NULL,
    workspace_id CHAR(36)                  NOT NULL,
    title        TEXT                      NOT NULL,
    icon         TEXT,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW(),
    CONSTRAINT document_pkey PRIMARY KEY (id)
);