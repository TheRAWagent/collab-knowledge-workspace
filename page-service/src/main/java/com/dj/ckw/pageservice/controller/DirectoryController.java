package com.dj.ckw.pageservice.controller;

import com.dj.ckw.pageservice.dto.DirectoryRequest;
import com.dj.ckw.pageservice.dto.DirectoryResponse;
import com.dj.ckw.pageservice.service.DirectoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/{workspaceId}/directories")
public class DirectoryController {

  private final DirectoryService directoryService;

  public DirectoryController(DirectoryService directoryService) {
    this.directoryService = directoryService;
  }

  @GetMapping(produces = "application/json")
  public Flux<DirectoryResponse> listDirectories(@PathVariable UUID workspaceId) {
    return directoryService.listDirectories(workspaceId);
  }

  @PostMapping(produces = "application/json")
  public Mono<ResponseEntity<DirectoryResponse>> createDirectory(
      @PathVariable UUID workspaceId,
      @Valid @RequestBody DirectoryRequest request) {
    return directoryService.createDirectory(workspaceId, request)
        .map(dir -> ResponseEntity
            .created(URI.create("/" + workspaceId + "/directories/" + dir.getId()))
            .body(dir));
  }

  @PatchMapping(path = "/{directoryId}", produces = "application/json")
  public Mono<DirectoryResponse> updateDirectory(
      @PathVariable UUID workspaceId,
      @PathVariable UUID directoryId,
      @Valid @RequestBody DirectoryRequest request) {
    return directoryService.updateDirectory(workspaceId, directoryId, request);
  }

  @DeleteMapping(path = "/{directoryId}")
  public Mono<Void> deleteDirectory(
      @PathVariable UUID workspaceId,
      @PathVariable UUID directoryId) {
    return directoryService.deleteDirectory(workspaceId, directoryId);
  }
}
