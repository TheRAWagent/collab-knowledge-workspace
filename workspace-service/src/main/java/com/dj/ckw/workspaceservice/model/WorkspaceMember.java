package com.dj.ckw.workspaceservice.model;

import com.dj.ckw.workspaceservice.enums.WorkspaceMemberRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "workspace_member")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WorkspaceMember {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Replace raw workspaceId with a ManyToOne relation to Workspace
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
}
