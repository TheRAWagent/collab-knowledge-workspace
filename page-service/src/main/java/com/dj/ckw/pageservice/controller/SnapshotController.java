package com.dj.ckw.pageservice.controller;

import com.dj.ckw.pageservice.dto.BlockSnapshotRequest;
import com.dj.ckw.pageservice.dto.SnapshotResponse;
import com.dj.ckw.pageservice.service.impl.SnapshotService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/internal/documents")
public class SnapshotController {

    private final SnapshotService snapshotService;

    public SnapshotController(SnapshotService snapshotService) {
        this.snapshotService = snapshotService;
    }

    @PutMapping("/{documentId}/snapshot")
    public Mono<Void> saveSnapshot(
            @PathVariable UUID documentId,
            @RequestBody BlockSnapshotRequest req
    ) {
        return snapshotService.persist(documentId, req);
    }

    @GetMapping("/{documentId}/snapshot")
    public Mono<SnapshotResponse> getSnapshot(
            @PathVariable UUID documentId
    ) {
        return snapshotService.getSnapshot(documentId);
    }
}
