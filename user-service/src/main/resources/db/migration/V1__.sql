CREATE TABLE users
(
    id         CHAR(36)     NOT NULL,
    avatar_url VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    email      VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT users_email_key UNIQUE (email);
