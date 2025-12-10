package com.dj.ckw.pageservice.controller;

import com.dj.ckw.pageservice.dto.PageRequest;
import com.dj.ckw.pageservice.dto.PageResponse;
import com.dj.ckw.pageservice.dto.validation.CreatePageValidationGroup;
import com.dj.ckw.pageservice.dto.validation.UpdatePageValidationGroup;
import com.dj.ckw.pageservice.service.PageService;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/{workspaceId}/pages")
public class PageController {

    private final PageService pageService;

    @PostMapping
    public Mono<ResponseEntity<PageResponse>> createPage(@RequestBody @Validated({Default.class, CreatePageValidationGroup.class}) PageRequest req, @PathVariable UUID workspaceId) {
        return pageService.createPage(req, workspaceId).map(page -> ResponseEntity.created(URI.create("/pages/" + page.getId())).body(page));
    }

    @GetMapping("/{pageId}")
    public Mono<PageResponse> getPage(@PathVariable UUID pageId, @PathVariable UUID workspaceId) {
        return pageService.getPage(pageId, workspaceId);
    }

    @GetMapping
    public Flux<PageResponse> getPages(@PathVariable UUID workspaceId) {
        return pageService.listPages(workspaceId);
    }

    @PatchMapping("/{pageId}")
    public Mono<PageResponse> updatePage(@PathVariable UUID pageId, @RequestBody @Validated({Default.class, UpdatePageValidationGroup.class}) PageRequest req, @PathVariable UUID workspaceId) {
        return pageService.updatePage(pageId, req, workspaceId);
    }

    @DeleteMapping("/{pageId}")
    public Mono<Void> deletePage(@PathVariable UUID pageId, @PathVariable UUID workspaceId) {
        return pageService.deletePage(pageId, workspaceId);
    }
}
