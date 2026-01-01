package com.dj.ckw.pageservice.service.impl;

import com.dj.ckw.pageservice.dto.DocumentRequest;
import com.dj.ckw.pageservice.dto.DocumentResponse;
import com.dj.ckw.pageservice.exception.DocumentNotFoundException;
import com.dj.ckw.pageservice.model.DocumentEntity;
import com.dj.ckw.pageservice.repository.DocumentRepository;
import com.dj.ckw.pageservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    public Mono<DocumentResponse> createPage(DocumentRequest req, UUID workspaceId) {
        return documentRepository.save(
                DocumentEntity.builder()
                        .workspaceId(workspaceId)
                        .title(req.getTitle())
                        .icon(req.getIcon())
                        .build()
        ).map(DocumentResponse::create);
    }

    public Mono<DocumentResponse> getPage(UUID pageId, UUID workspaceId) {
        return documentRepository.findById(pageId).filter(document -> document.getWorkspaceId().equals(workspaceId)).map(DocumentResponse::create).switchIfEmpty(Mono.error(new DocumentNotFoundException(pageId)));
    }

    public Flux<DocumentResponse> listPages(UUID workspaceId) {
        return documentRepository.findAllByWorkspaceId(workspaceId).map(DocumentResponse::create);
    }

    public Mono<DocumentResponse> updatePage(UUID pageId, DocumentRequest req, UUID workspaceId) {
        return documentRepository.findById(pageId).filter(document -> document.getWorkspaceId().equals(workspaceId)).switchIfEmpty(Mono.error(new DocumentNotFoundException(pageId))).flatMap(existing -> {
            if (req.getTitle() != null && !req.getTitle().isEmpty()) {
                existing.setTitle(req.getTitle());
            }
            if (req.getIcon() != null && !req.getIcon().isEmpty()) {
                existing.setIcon(req.getIcon());
            }
            return documentRepository.save(existing).map(DocumentResponse::create);
        });
    }

    public Mono<Void> deletePage(UUID pageId, UUID workspaceId) {
        return this.getPage(pageId, workspaceId).flatMap(
                existing -> documentRepository.deleteById(existing.getId())
        );
    }
}
