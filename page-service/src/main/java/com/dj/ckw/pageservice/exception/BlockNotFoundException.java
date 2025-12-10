package com.dj.ckw.pageservice.exception;

import java.util.UUID;

public class BlockNotFoundException extends RuntimeException {
    public BlockNotFoundException(UUID id) {
        super("Block not found: " + id);
    }
}
