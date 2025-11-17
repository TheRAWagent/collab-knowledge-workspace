package com.dj.ckw.workspaceservice.utils;

import com.dj.ckw.workspaceservice.dto.CreateWorkspaceRequest;
import com.dj.ckw.workspaceservice.dto.WorkspaceResponse;
import com.dj.ckw.workspaceservice.model.Workspace;

import java.util.Date;

public final class WorkspaceMapper {

    private WorkspaceMapper() {}

    public static Workspace toEntity(CreateWorkspaceRequest req, String ownerId) {
        Workspace w = new Workspace();
        w.setName(req.getName());
        w.setDescription(req.getDescription());
        w.setOwnerId(ownerId);
        w.setCreatedAt(new Date());
        w.setUpdatedAt(new Date());
        return w;
    }

    public static WorkspaceResponse toDto(Workspace w) {
        WorkspaceResponse r = new WorkspaceResponse();
        r.setId(w.getId());
        r.setName(w.getName());
        r.setDescription(w.getDescription());
        r.setOwnerId(w.getOwnerId());
        r.setCreatedAt(w.getCreatedAt());
        r.setUpdatedAt(w.getUpdatedAt());
        return r;
    }
}
