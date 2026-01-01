package com.dj.ckw.pageservice.repository;

import com.dj.ckw.pageservice.model.DocumentEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DocumentRepository extends R2dbcRepository<DocumentEntity, UUID> {
    Flux<DocumentEntity> findAllByWorkspaceId(UUID workspaceId);

    Mono<DocumentEntity> findByIdAndVersion(UUID id, int version);
}
