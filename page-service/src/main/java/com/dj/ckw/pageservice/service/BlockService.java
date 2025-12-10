package com.dj.ckw.pageservice.service;

import com.dj.ckw.pageservice.dto.BlockRequest;
import com.dj.ckw.pageservice.dto.BlockResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface BlockService {
    Mono<BlockResponse> createBlock(UUID pageId, BlockRequest req);

    Flux<BlockResponse> getBlocksForPage(UUID pageId);

    Mono<BlockResponse> updateBlock(UUID pageId, UUID blockId, BlockRequest req);

    Mono<Void> deleteBlock(UUID blockId);
}
