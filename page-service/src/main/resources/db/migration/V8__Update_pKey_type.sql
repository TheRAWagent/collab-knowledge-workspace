-- Change char(36) colums to UUID type;

-- First, drop foreign key constraints that depend on the columns being altered
ALTER TABLE blocks DROP CONSTRAINT IF EXISTS block_document_id_fkey;
ALTER TABLE blocks DROP CONSTRAINT IF EXISTS block_parent_id_fkey;

-- Drop primary key constraints to allow column type alteration
ALTER TABLE documents DROP CONSTRAINT IF EXISTS document_pkey;
ALTER TABLE blocks DROP CONSTRAINT IF EXISTS block_pkey;

ALTER TABLE documents ALTER COLUMN id DROP DEFAULT;
ALTER TABLE blocks ALTER COLUMN id DROP DEFAULT;

-- Alter the column types from CHAR(36) to UUID
ALTER TABLE documents
    ALTER COLUMN id TYPE UUID USING trim(id)::UUID,
    ALTER COLUMN workspace_id TYPE UUID USING trim(workspace_id)::UUID;

ALTER TABLE blocks
    ALTER COLUMN id TYPE UUID USING trim(id)::UUID,
    ALTER COLUMN document_id TYPE UUID USING trim(document_id)::UUID,
    ALTER COLUMN parent_id TYPE UUID USING trim(parent_id)::UUID;

-- Restore the default values for the UUID columns
ALTER TABLE documents
    ALTER COLUMN id SET DEFAULT uuidv7();
ALTER TABLE documents
    ALTER COLUMN id SET NOT NULL;
ALTER TABLE blocks
    ALTER COLUMN id SET DEFAULT uuidv7();
ALTER TABLE blocks
    ALTER COLUMN id SET NOT NULL;

-- Recreate the primary key constraints with the updated column types
ALTER TABLE documents
    ADD CONSTRAINT documents_pkey PRIMARY KEY (id);
ALTER TABLE blocks
    ADD CONSTRAINT blocks_pkey PRIMARY KEY (id);

-- Recreate the foreign key constraints with the updated column types
ALTER TABLE blocks
    ADD CONSTRAINT blocks_document_id_fkey FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE;
ALTER TABLE blocks
    ADD CONSTRAINT block_parent_id_fkey FOREIGN KEY (parent_id) REFERENCES blocks (id) ON DELETE CASCADE;