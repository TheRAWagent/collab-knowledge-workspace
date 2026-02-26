package com.dj.ckw.pageservice.service.impl;

import com.dj.ckw.pageservice.dto.DirectoryRequest;
import com.dj.ckw.pageservice.dto.DirectoryResponse;
import com.dj.ckw.pageservice.exception.DocumentNotFoundException;
import com.dj.ckw.pageservice.model.DirectoryEntity;
import com.dj.ckw.pageservice.repository.DirectoryRepository;
import com.dj.ckw.pageservice.service.DirectoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DirectoryServiceImpl implements DirectoryService {

  private static final Logger log = LoggerFactory.getLogger(DirectoryServiceImpl.class);

  private final DirectoryRepository directoryRepository;

  public DirectoryServiceImpl(DirectoryRepository directoryRepository) {
    this.directoryRepository = directoryRepository;
  }

  @Override
  public Flux<DirectoryResponse> listDirectories(UUID workspaceId) {
    return directoryRepository
        .findAllByWorkspaceIdOrderBySortOrderAsc(workspaceId)
        .map(DirectoryResponse::from);
  }

  @Override
  public Mono<DirectoryResponse> createDirectory(UUID workspaceId, DirectoryRequest request) {
    // compute sort order = current sibling count
    Mono<Integer> siblingCount = directoryRepository
        .countByWorkspaceIdAndParentId(workspaceId, request.getParentId())
        .defaultIfEmpty(0);

    return siblingCount.flatMap(count -> {
      DirectoryEntity entity = new DirectoryEntity();
      entity.setWorkspaceId(workspaceId);
      entity.setParentId(request.getParentId());
      entity.setName(request.getName());
      entity.setSortOrder(request.getSortOrder() != null ? request.getSortOrder() : count);
      return directoryRepository.save(entity);
    }).map(DirectoryResponse::from)
        .doOnSuccess(d -> log.info("Directory created: {} in workspace {}", d.getId(), workspaceId));
  }

  @Override
  public Mono<DirectoryResponse> updateDirectory(UUID workspaceId, UUID directoryId, DirectoryRequest request) {
    return directoryRepository.findById(directoryId)
        .filter(d -> d.getWorkspaceId().equals(workspaceId))
        .switchIfEmpty(Mono.error(new DocumentNotFoundException(directoryId)))
        .flatMap(existing -> {
          if (request.getName() != null && !request.getName().isBlank()) {
            existing.setName(request.getName());
          }
          if (request.getParentId() != null) {
            existing.setParentId(request.getParentId());
          }
          if (request.getSortOrder() != null) {
            existing.setSortOrder(request.getSortOrder());
          }
          return directoryRepository.save(existing);
        })
        .map(DirectoryResponse::from)
        .doOnSuccess(d -> log.info("Directory updated: {}", directoryId));
  }

  @Override
  public Mono<Void> deleteDirectory(UUID workspaceId, UUID directoryId) {
    return directoryRepository.findById(directoryId)
        .filter(d -> d.getWorkspaceId().equals(workspaceId))
        .switchIfEmpty(Mono.error(new DocumentNotFoundException(directoryId)))
        .flatMap(d -> directoryRepository.deleteById(d.getId()))
        .doOnSuccess(v -> log.info("Directory deleted: {}", directoryId));
  }
}
