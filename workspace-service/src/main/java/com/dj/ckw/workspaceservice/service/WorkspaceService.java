package com.dj.ckw.workspaceservice.service;

import com.dj.ckw.workspaceservice.dto.*;
import com.dj.ckw.workspaceservice.enums.WorkspaceMemberRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkspaceService {
    WorkspaceResponse create(CreateWorkspaceRequest req, String ownerId);
    WorkspaceResponse getById(UUID id, String requesterId);
    WorkspaceResponse update(UUID id, UpdateWorkspaceRequest req, String requesterId);
    void delete(UUID id, String requesterId);
    Optional<WorkspaceMemberRole> getWorkspaceMemberRole(String workspaceId, String requesterId);

    // New paged listing: page is 1-based for client, size default handled by controller
    PagedResponse<WorkspaceResponse> listByUserPaged(String ownerId, int page, int size, String search);
    WorkspaceMembersResponseDto getWorkspaceMembers(UUID workspaceId, String requesterId);
    void transferOwnership(UUID workspaceId, String newOwnerId, String requesterId);
}
