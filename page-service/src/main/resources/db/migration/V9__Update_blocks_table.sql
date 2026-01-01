ALTER TABLE blocks
    DROP COLUMN content;

ALTER TABLE blocks
    DROP COLUMN position;

ALTER TABLE blocks
    ADD COLUMN text TEXT;

ALTER TABLE blocks
    ADD COLUMN order_index BIGINT NOT NULL default 0;

ALTER TABLE blocks
    ALTER COLUMN attrs SET DEFAULT '{}'::jsonb;
