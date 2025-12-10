package com.dj.ckw.pageservice.exception;

import java.util.UUID;

public class PageNotFoundException extends RuntimeException {
    public PageNotFoundException(UUID id) {
        super("Page not found: " + id);
    }
}
