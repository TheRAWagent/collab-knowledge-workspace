package com.dj.ckw.pageservice.exception;

import java.util.UUID;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(UUID id) {
        super("Page not found: " + id);
    }
}
