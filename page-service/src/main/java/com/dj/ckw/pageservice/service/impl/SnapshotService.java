package com.dj.ckw.pageservice.service.impl;

import com.dj.ckw.pageservice.dto.BlockSnapshotRequest;
import com.dj.ckw.pageservice.dto.SnapshotResponse;
import com.dj.ckw.pageservice.model.Snapshot;
import com.dj.ckw.pageservice.repository.SnapshotRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SnapshotService {

    private static final Logger log = LoggerFactory.getLogger(SnapshotService.class);
    private final SnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;

    public Mono<Void> persist(
            UUID documentId,
            BlockSnapshotRequest req
    ) {
        try {
            String contentJson;
            try {
                contentJson = objectMapper.writeValueAsString(req.getContent());
                JsonNode normalized = objectMapper.readTree(contentJson);
                contentJson = normalized.toString();
            } catch (JsonProcessingException e) {
                log.error("Error serializing snapshot content", e);
                return Mono.error(new RuntimeException("Failed to serialize snapshot content", e));
            }

            String finalContentJson = contentJson;
            return snapshotRepository.findByDocumentId(documentId)
                    .flatMap(existing -> {
                        // UPDATE CASE
                        if (existing.getVersion() >= req.getVersion()) {
                            log.info("Existing snapshot version {} is greater than or equal to request version {}, skipping update.", existing.getVersion(), req.getVersion());
                            return Mono.just(existing);
                        }
                        existing.setVersion((long) req.getVersion());
                        existing.setSchemaVersion((long) req.getSchemaVersion());
                        existing.setGeneratedAt(req.getGeneratedAt());
                        existing.setUpdatedBy(req.getUpdatedBy());
                        existing.setSource(req.getSource());
                        existing.setContentJson(Json.of(finalContentJson));
                        return snapshotRepository.save(existing);
                    })
                    .switchIfEmpty(
                            // INSERT CASE
                            Mono.defer(() -> {
                                        Snapshot snapshot = new Snapshot();
                                        snapshot.setDocumentId(documentId);
                                        snapshot.setVersion((long) req.getVersion());
                                        snapshot.setSchemaVersion((long) req.getSchemaVersion());
                                        snapshot.setGeneratedAt(req.getGeneratedAt());
                                        snapshot.setUpdatedBy(req.getUpdatedBy());
                                        snapshot.setSource(req.getSource());
                                        snapshot.setContentJson(Json.of(finalContentJson));
                                        return snapshotRepository.save(snapshot);
                                    }
                            )
                    )
                    .then();
        } catch (Exception e) {
            log.error("Error persisting snapshot for documentId: {}", documentId, e);
            log.error("Request data: {}", req.toString(), e);
            log.error("Exception message: {}", e.getMessage(), e);
            log.error("Stack trace: ", e);
            return Mono.error(new RuntimeException("Failed to persist snapshot", e));
        }
    }

    public Mono<SnapshotResponse> getSnapshot(UUID documentId) {
        return snapshotRepository.findByDocumentId(documentId)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Snapshot not found for documentId: " + documentId)))
                .map(snap -> new SnapshotResponse(
                        snap.getVersion(),
                        snap.getSchemaVersion(),
                        snap.getGeneratedAt(),
                        snap.getUpdatedBy(),
                        snap.getSource(),
                        snap.getContentJson().asString()
                ));
    }

}
