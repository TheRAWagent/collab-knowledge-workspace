package com.dj.ckw.workspaceservice.controller;

import com.dj.ckw.workspaceservice.dto.*;
import com.dj.ckw.workspaceservice.model.RequestInfo;
import com.dj.ckw.workspaceservice.model.WorkspaceMember;
import com.dj.ckw.workspaceservice.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class WorkspaceController {

    private final WorkspaceService service;
    private final RequestInfo requestInfo;

    public WorkspaceController(WorkspaceService service, RequestInfo requestInfo) {
        this.service = service;
        this.requestInfo = requestInfo;
    }

    @Operation(summary = "Create a new workspace")
    @PostMapping
    public ResponseEntity<WorkspaceResponse> create(@Valid @RequestBody CreateWorkspaceRequest req) {
        String owner = requestInfo.getEmail();
        WorkspaceResponse created = service.create(req, owner);
        return ResponseEntity.created(URI.create("/workspaces/" + created.getId())).body(created);
    }

    @Operation(summary = "Get workspace by ID")
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> getById(@PathVariable UUID id) {
        String requester = requestInfo.getEmail();
        return ResponseEntity.ok().body(service.getById(id, requester));
    }

    @Operation(summary = "List workspaces with pagination and optional search")
    @GetMapping
    public ResponseEntity<PagedResponse<WorkspaceResponse>> list(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size,
            @RequestParam(name = "search", required = false) String search
    ) {
        String owner = requestInfo.getEmail();
        return ResponseEntity.ok().body(service.listByUserPaged(owner, page, size, search));
    }

    @Operation(summary = "Update an existing workspace")
    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateWorkspaceRequest req) {
        String requester = requestInfo.getEmail();
        return ResponseEntity.ok().body(service.update(id, req, requester));
    }

    @Operation(summary = "Delete a workspace by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        String requester = requestInfo.getEmail();
        service.delete(id, requester);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<WorkspaceMembersResponseDto> getMembers(@PathVariable UUID id) {
        return ResponseEntity.ok().body(service.getWorkspaceMembers(id, requestInfo.getEmail()));
    }

    @PostMapping("/{id}/transfer-ownership")
    public ResponseEntity<Void> transferOwnership(@PathVariable UUID id, @RequestParam String newOwnerId) {
        service.transferOwnership(id, newOwnerId, requestInfo.getEmail());
        return ResponseEntity.noContent().build();
    }
}
