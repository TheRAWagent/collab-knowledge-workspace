package com.dj.ckw.pageservice.repository;

import com.dj.ckw.pageservice.model.SnapshotEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface SnapshotRepository extends R2dbcRepository<SnapshotEntity, UUID> {
    Mono<SnapshotEntity> findByDocumentId(UUID documentId);
}
