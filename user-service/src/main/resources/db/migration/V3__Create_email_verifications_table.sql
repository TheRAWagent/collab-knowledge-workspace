CREATE TABLE email_verifications (
    id UUID NOT NULL PRIMARY KEY DEFAULT uuidv7(),
    email VARCHAR(255) NOT NULL,
    verification_code_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    purpose VARCHAR(50) NOT NULL,
    attempts INT NOT NULL DEFAULT 0,
    max_attempts INT NOT NULL DEFAULT 5,
    CONSTRAINT email_verifications_email_purpose_key UNIQUE (email, purpose)
);