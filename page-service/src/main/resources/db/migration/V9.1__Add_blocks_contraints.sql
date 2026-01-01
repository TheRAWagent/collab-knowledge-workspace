CREATE UNIQUE INDEX uq_blocks_parent_order
    ON blocks (parent_id, order_index);

ALTER TABLE blocks
    ALTER COLUMN attrs SET NOT NULL;
