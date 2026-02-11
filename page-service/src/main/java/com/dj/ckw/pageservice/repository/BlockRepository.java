package com.dj.ckw.pageservice.repository;

import com.dj.ckw.pageservice.model.BlockEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface BlockRepository extends R2dbcRepository<BlockEntity, UUID> {
}
