package com.dj.ckw.pageservice.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record BlockResponse(
        UUID id,
        UUID parentId,   // null = root-level block
        String type,
        JsonNode content, // rich JSON content (TipTap style)
        JsonNode attrs,
        String position
) {
}
