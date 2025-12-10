package com.dj.ckw.workspaceservice.utils;

import com.dj.ckw.workspaceservice.dto.CreateWorkspaceRequest;
import com.dj.ckw.workspaceservice.dto.WorkspaceResponse;
import com.dj.ckw.workspaceservice.model.Workspace;

import java.util.Date;

public final class WorkspaceMapper {

    private WorkspaceMapper() {
    }

    public static Workspace toEntity(CreateWorkspaceRequest req, String ownerId) {
        return Workspace.builder()
                .name(req.getName())
                .description(req.getDescription())
                .ownerId(ownerId)
                .description(req.getDescription())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public static WorkspaceResponse toDto(Workspace w) {
        return WorkspaceResponse.builder()
                .id(w.getId())
                .name(w.getName())
                .description(w.getDescription())
                .ownerId(w.getOwnerId())
                .createdAt(w.getCreatedAt())
                .updatedAt(w.getUpdatedAt())
                .build();
    }
}
