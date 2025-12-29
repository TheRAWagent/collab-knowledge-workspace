CREATE INDEX idx_blocks_document ON block(document_id);
CREATE INDEX idx_blocks_parent ON block(parent_id);
CREATE UNIQUE INDEX idx_blocks_order
    ON block(document_id, parent_id, position);