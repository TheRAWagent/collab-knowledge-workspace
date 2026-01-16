CREATE TABLE domain_events
(
    id           UUID         NOT NULL PRIMARY KEY DEFAULT uuidv7(),
    event_type   VARCHAR(255) NOT NULL,
    payload      JSONB        NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    event_status VARCHAR(50) NOT NULL,
    retries      INT          NOT NULL             DEFAULT 0,
    processed_at TIMESTAMP WITHOUT TIME ZONE
);