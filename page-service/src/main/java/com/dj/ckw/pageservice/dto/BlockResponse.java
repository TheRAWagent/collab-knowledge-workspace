package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.enums.BlockType;
import com.dj.ckw.pageservice.model.Block;
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
public class BlockResponse {
    @NotNull
    private UUID id;

    @NotNull
    private UUID pageId;

    private UUID parentId;   // null = root-level block

    @NotNull
    private BlockType type;

    @NotNull
    private String contentJson; // rich JSON content (TipTap style)

    @NotNull
    private Integer orderIndex;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    public static BlockResponse create(Block block) {
        return BlockResponse.builder()
                .id(block.getId())
                .pageId(block.getPageId())
                .parentId(block.getParentId())
                .type(block.getType())
                .contentJson(block.getContentJson())
                .orderIndex(block.getOrderIndex())
                .createdAt(block.getCreatedAt())
                .updatedAt(block.getUpdatedAt())
                .build();
    }
}
