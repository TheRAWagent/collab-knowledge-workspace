package com.dj.ckw.pageservice.service;

import com.dj.ckw.pageservice.dto.DirectoryRequest;
import com.dj.ckw.pageservice.dto.DirectoryResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface DirectoryService {

  Flux<DirectoryResponse> listDirectories(UUID workspaceId);

  Mono<DirectoryResponse> createDirectory(UUID workspaceId, DirectoryRequest request);

  Mono<DirectoryResponse> updateDirectory(UUID workspaceId, UUID directoryId, DirectoryRequest request);

  Mono<Void> deleteDirectory(UUID workspaceId, UUID directoryId);
}
