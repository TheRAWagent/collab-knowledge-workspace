package com.dj.ckw.pageservice.service;

import com.dj.ckw.pageservice.dto.DocumentRequest;
import com.dj.ckw.pageservice.dto.DocumentResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DocumentService {
    Mono<DocumentResponse> createPage(DocumentRequest req, UUID workspaceId);
    Mono<DocumentResponse> getPage(UUID pageId, UUID workspaceId);
    Flux<DocumentResponse> listPages(UUID workspaceId);
    Mono<DocumentResponse> updatePage(UUID pageId, DocumentRequest req, UUID workspaceId);
    Mono<Void> deletePage(UUID pageId, UUID workspaceId);
}
