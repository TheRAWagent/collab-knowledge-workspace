package com.dj.ckw.pageservice.controller;

import com.dj.ckw.pageservice.dto.BlockRequest;
import com.dj.ckw.pageservice.dto.BlockResponse;
import com.dj.ckw.pageservice.dto.validation.CreateBlockValidationGroup;
import com.dj.ckw.pageservice.service.impl.BlockServiceImpl;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/{workspaceId}/pages/{pageId}/blocks")
public class BlockController {

    private final BlockServiceImpl blockServiceImpl;

    public BlockController(BlockServiceImpl blockServiceImpl) {
        this.blockServiceImpl = blockServiceImpl;
    }

    @PostMapping
    public Mono<BlockResponse> createBlock(@PathVariable UUID pageId, @RequestBody @Validated({Default.class, CreateBlockValidationGroup.class}) BlockRequest req, @PathVariable String workspaceId) {
        return blockServiceImpl.createBlock(pageId, req);
    }

    @GetMapping
    public Flux<BlockResponse> getBlocks(@PathVariable UUID pageId, @PathVariable String workspaceId) {
        return blockServiceImpl.getBlocksForPage(pageId);
    }

    @PatchMapping("/{blockId}")
    public Mono<BlockResponse> updateBlock(@PathVariable UUID pageId, @PathVariable UUID blockId, @RequestBody @Validated BlockRequest req, @PathVariable String workspaceId) {
        return blockServiceImpl.updateBlock(pageId, blockId, req);
    }

    @DeleteMapping("/{blockId}")
    public Mono<Void> deleteBlock(@PathVariable UUID blockId, @PathVariable String pageId, @PathVariable String workspaceId) {
        return blockServiceImpl.deleteBlock(blockId);
    }
}
