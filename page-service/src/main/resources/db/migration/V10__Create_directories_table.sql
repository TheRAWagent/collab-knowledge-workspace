-- Create directories table for organizing pages within a workspace
CREATE TABLE directories (
  id UUID DEFAULT uuidv7() NOT NULL,
  workspace_id UUID NOT NULL,
  parent_id UUID,
  name TEXT NOT NULL,
  sort_order INT DEFAULT 0 NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  CONSTRAINT directories_pkey PRIMARY KEY (id),
  CONSTRAINT directories_parent_fk FOREIGN KEY (parent_id) REFERENCES directories (id) ON DELETE CASCADE
);

CREATE INDEX idx_directories_workspace_id ON directories (workspace_id);
CREATE INDEX idx_directories_parent_id ON directories (parent_id);

-- Add directory_id and sort_order to documents table
ALTER TABLE documents
ADD COLUMN directory_id UUID REFERENCES directories (id) ON DELETE
SET NULL;

ALTER TABLE documents
ADD COLUMN sort_order INT DEFAULT 0 NOT NULL;
CREATE INDEX idx_documents_directory_id ON documents (directory_id);
