package com.dj.ckw.pageservice.repository;

import com.dj.ckw.pageservice.model.DirectoryEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DirectoryRepository extends R2dbcRepository<DirectoryEntity, UUID> {

  Flux<DirectoryEntity> findAllByWorkspaceIdOrderBySortOrderAsc(UUID workspaceId);

  Flux<DirectoryEntity> findAllByWorkspaceIdAndParentIdOrderBySortOrderAsc(UUID workspaceId, UUID parentId);

  Mono<Integer> countByWorkspaceIdAndParentId(UUID workspaceId, UUID parentId);
}
