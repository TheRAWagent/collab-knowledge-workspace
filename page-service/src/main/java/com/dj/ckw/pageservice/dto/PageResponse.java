package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.model.DocumentEntity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class PageResponse {
    @NotNull
    private UUID id;

    @NotNull
    private UUID workspaceId;

    @NotNull
    private String title;

    @NotNull
    private String icon;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    public static PageResponse create(DocumentEntity documentEntity) {
        return PageResponse.builder()
                .id(documentEntity.getId())
                .workspaceId(documentEntity.getWorkspaceId())
                .title(documentEntity.getTitle())
                .icon(documentEntity.getIcon())
                .createdAt(documentEntity.getCreatedAt())
                .updatedAt(documentEntity.getUpdatedAt())
                .build();
    }
}
