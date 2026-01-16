ALTER TABLE domain_events
    ALTER
        COLUMN created_at TYPE TIMESTAMPTZ;

ALTER TABLE domain_events
    ALTER
        COLUMN created_at SET DEFAULT NOW();
