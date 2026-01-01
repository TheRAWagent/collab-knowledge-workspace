package com.dj.ckw.pageservice.service.impl;

import com.dj.ckw.pageservice.dto.PageRequest;
import com.dj.ckw.pageservice.dto.PageResponse;
import com.dj.ckw.pageservice.exception.PageNotFoundException;
import com.dj.ckw.pageservice.model.DocumentEntity;
import com.dj.ckw.pageservice.repository.DocumentRepository;
import com.dj.ckw.pageservice.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PageServiceImpl implements PageService {

    private final DocumentRepository documentRepository;

    public Mono<PageResponse> createPage(PageRequest req, UUID workspaceId) {
        return documentRepository.save(
                DocumentEntity.builder()
                        .workspaceId(workspaceId)
                        .title(req.getTitle())
                        .icon(req.getIcon())
                        .build()
        ).map(PageResponse::create);
    }

    public Mono<PageResponse> getPage(UUID pageId, UUID workspaceId) {
        return documentRepository.findById(pageId).filter(document -> document.getWorkspaceId().equals(workspaceId)).map(PageResponse::create).switchIfEmpty(Mono.error(new PageNotFoundException(pageId)));
    }

    public Flux<PageResponse> listPages(UUID workspaceId) {
        return documentRepository.findAllByWorkspaceId(workspaceId).map(PageResponse::create);
    }

    public Mono<PageResponse> updatePage(UUID pageId, PageRequest req, UUID workspaceId) {
        return documentRepository.findById(pageId).filter(document -> document.getWorkspaceId().equals(workspaceId)).switchIfEmpty(Mono.error(new PageNotFoundException(pageId))).flatMap(existing -> {
            if (req.getTitle() != null && !req.getTitle().isEmpty()) {
                existing.setTitle(req.getTitle());
            }
            if (req.getIcon() != null && !req.getIcon().isEmpty()) {
                existing.setIcon(req.getIcon());
            }
            return documentRepository.save(existing).map(PageResponse::create);
        });
    }

    public Mono<Void> deletePage(UUID pageId, UUID workspaceId) {
        return this.getPage(pageId, workspaceId).flatMap(
                existing -> documentRepository.deleteById(existing.getId())
        );
    }
}
