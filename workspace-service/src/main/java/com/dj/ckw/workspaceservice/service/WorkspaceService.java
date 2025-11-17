package com.dj.ckw.workspaceservice.service;

import com.dj.ckw.workspaceservice.dto.CreateWorkspaceRequest;
import com.dj.ckw.workspaceservice.dto.PagedResponse;
import com.dj.ckw.workspaceservice.dto.UpdateWorkspaceRequest;
import com.dj.ckw.workspaceservice.dto.WorkspaceResponse;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {
    WorkspaceResponse create(CreateWorkspaceRequest req, String ownerId);
    WorkspaceResponse getById(UUID id, String requesterId);
    List<WorkspaceResponse> listByOwner(String ownerId);
    WorkspaceResponse update(UUID id, UpdateWorkspaceRequest req, String requesterId);
    void delete(UUID id, String requesterId);

    // New paged listing: page is 1-based for client, size default handled by controller
    PagedResponse<WorkspaceResponse> listByOwnerPaged(String ownerId, int page, int size, String search);
}
