package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.model.Page;
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

    public static PageResponse create(Page page) {
        return PageResponse.builder()
                .id(page.getId())
                .workspaceId(page.getWorkspaceId())
                .title(page.getTitle())
                .icon(page.getIcon())
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .build();
    }
}
