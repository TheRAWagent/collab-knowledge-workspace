package com.dj.ckw.pageservice.controller;

import com.dj.ckw.pageservice.dto.*;
import com.dj.ckw.pageservice.dto.validation.CreateDocumentValidationGroup;
import com.dj.ckw.pageservice.dto.validation.UpdateDocumentValidationGroup;
import com.dj.ckw.pageservice.service.DocumentService;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/{workspaceId}/documents")
public class DocumentController {

  private final DocumentService documentService;

  public DocumentController(DocumentService documentService) {
    this.documentService = documentService;
  }

  @PostMapping(produces = "application/json")
  public Mono<ResponseEntity<DocumentResponse>> createPage(
      @RequestBody @Validated({ Default.class, CreateDocumentValidationGroup.class }) DocumentRequest req,
      @PathVariable UUID workspaceId) {
    return documentService.createPage(req, workspaceId)
        .map(page -> ResponseEntity.created(URI.create("/pages/" + page.getId())).body(page));
  }

  @GetMapping(path = "/{pageId}", produces = "application/json")
  public Mono<DocumentResponse> getPage(@PathVariable UUID pageId, @PathVariable UUID workspaceId) {
    return documentService.getPage(pageId, workspaceId);
  }

  @GetMapping(produces = "application/json")
  public Flux<DocumentResponse> getPages(@PathVariable UUID workspaceId) {
    return documentService.listPages(workspaceId);
  }

  @PatchMapping(path = "/{pageId}", produces = "application/json")
  public Mono<DocumentResponse> updatePage(@PathVariable UUID pageId,
      @RequestBody @Validated({ Default.class, UpdateDocumentValidationGroup.class }) DocumentRequest req,
      @PathVariable UUID workspaceId) {
    return documentService.updatePage(pageId, req, workspaceId);
  }

  @DeleteMapping(path = "/{pageId}")
  public Mono<Void> deletePage(@PathVariable UUID pageId, @PathVariable UUID workspaceId) {
    return documentService.deletePage(pageId, workspaceId);
  }

  @PatchMapping(path = "/{pageId}/move", produces = "application/json")
  public Mono<DocumentResponse> movePage(
      @PathVariable UUID pageId,
      @PathVariable UUID workspaceId,
      @RequestBody MovePageRequest request) {
    return documentService.movePage(pageId, workspaceId, request);
  }
}
