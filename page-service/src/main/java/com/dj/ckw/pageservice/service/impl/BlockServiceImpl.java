package com.dj.ckw.pageservice.service.impl;

import com.dj.ckw.pageservice.dto.BlockRequest;
import com.dj.ckw.pageservice.dto.BlockResponse;
import com.dj.ckw.pageservice.exception.BlockNotFoundException;
import com.dj.ckw.pageservice.model.Block;
import com.dj.ckw.pageservice.repository.BlockRepository;
import com.dj.ckw.pageservice.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;

    public Mono<BlockResponse> createBlock(UUID pageId, BlockRequest req) {
        return blockRepository.save(
                Block.builder()
                        .pageId(pageId)
                        .parentId(req.getParentId())
                        .type(req.getType())
                        .contentJson(req.getContentJson())
                        .orderIndex(req.getOrderIndex())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).map(BlockResponse::create);
    }

    public Flux<BlockResponse> getBlocksForPage(UUID pageId) {
        return blockRepository.findAllByPageIdOrderByOrderIndexAsc(pageId).map(BlockResponse::create);
    }

    public Mono<BlockResponse> updateBlock(UUID pageId, UUID blockId, BlockRequest req) {
        return blockRepository.findById(blockId)
                .filter(block -> block.getPageId().equals(pageId))
                .switchIfEmpty(Mono.error(new BlockNotFoundException(blockId)))
                .flatMap(b -> {
                    b.setContentJson(req.getContentJson());
                    b.setUpdatedAt(LocalDateTime.now());
                    return blockRepository.save(b).map(BlockResponse::create);
                });
    }

    public Mono<Void> deleteBlock(UUID blockId) {
        return blockRepository.deleteById(blockId);
    }
}
