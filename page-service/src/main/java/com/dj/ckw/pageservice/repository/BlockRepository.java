package com.dj.ckw.pageservice.repository;

import com.dj.ckw.pageservice.model.Block;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface BlockRepository extends R2dbcRepository<Block, UUID> {
    Flux<Block> findAllByPageIdOrderByOrderIndexAsc(UUID pageId);

    Flux<Block> findAllByParentIdOrderByOrderIndexAsc(UUID parentId);
}
