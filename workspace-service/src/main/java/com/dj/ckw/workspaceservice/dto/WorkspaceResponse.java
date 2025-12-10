package com.dj.ckw.workspaceservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceResponse {
    @NotNull
    private UUID id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String ownerId;
    @NotNull
    private Date createdAt;
    @NotNull
    private Date updatedAt;
}

