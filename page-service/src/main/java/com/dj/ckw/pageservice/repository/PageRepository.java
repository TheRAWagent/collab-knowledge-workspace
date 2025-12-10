package com.dj.ckw.pageservice.repository;

import com.dj.ckw.pageservice.model.Page;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface PageRepository extends R2dbcRepository<Page, UUID> {
    Flux<Page> findAllByWorkspaceId(UUID workspaceId);
}
