package com.dj.ckw.workspaceservice.model;

import com.dj.ckw.workspaceservice.enums.WorkspaceMemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "workspace_member")
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Replace raw workspaceId with a ManyToOne relation to Workspace
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    @NotNull(message = "Workspace is required")
    private Workspace workspace;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID is required")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Role is required")
    private WorkspaceMemberRole role;

    // No-arg constructor required by JPA
    public WorkspaceMember() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public WorkspaceMemberRole getRole() {
        return role;
    }

    public void setRole(WorkspaceMemberRole role) {
        this.role = role;
    }
}
