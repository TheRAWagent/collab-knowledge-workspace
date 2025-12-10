package com.dj.ckw.pageservice.service;

import com.dj.ckw.pageservice.dto.PageRequest;
import com.dj.ckw.pageservice.dto.PageResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PageService {
    Mono<PageResponse> createPage(PageRequest req, UUID workspaceId);
    Mono<PageResponse> getPage(UUID pageId, UUID workspaceId);
    Flux<PageResponse> listPages(UUID workspaceId);
    Mono<PageResponse> updatePage(UUID pageId,PageRequest req, UUID workspaceId);
    Mono<Void> deletePage(UUID pageId, UUID workspaceId);
}
