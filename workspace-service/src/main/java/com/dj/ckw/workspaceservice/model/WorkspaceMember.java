package com.dj.ckw.workspaceservice.model;

import com.dj.ckw.workspaceservice.enums.WorkspaceMemberRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Entity
@Table(name = "workspace_member")
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    @JsonIgnore
    @NotNull(message = "Workspace is required")
    private Workspace workspace;

    @Column(name = "user_id", nullable = false)
    @NotNull(message = "User ID is required")
    @Email
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @NotNull(message = "Role is required")
    private WorkspaceMemberRole role;

    public WorkspaceMember() {
    }

    public WorkspaceMember(UUID id, Workspace workspace, String userId, WorkspaceMemberRole role) {
        this.id = id;
        this.workspace = workspace;
        this.userId = userId;
        this.role = role;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public WorkspaceMemberRole getRole() {
        return role;
    }

    public void setRole(WorkspaceMemberRole role) {
        this.role = role;
    }

    public static WorkspaceMemberBuilder builder() {
        return new WorkspaceMemberBuilder();
    }

    public static class WorkspaceMemberBuilder {
        private UUID id;
        private Workspace workspace;
        private String userId;
        private WorkspaceMemberRole role;

        public WorkspaceMemberBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public WorkspaceMemberBuilder workspace(Workspace workspace) {
            this.workspace = workspace;
            return this;
        }

        public WorkspaceMemberBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public WorkspaceMemberBuilder role(WorkspaceMemberRole role) {
            this.role = role;
            return this;
        }

        public WorkspaceMember build() {
            return new WorkspaceMember(id, workspace, userId, role);
        }
    }
}
