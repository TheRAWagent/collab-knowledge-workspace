package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.dto.validation.CreateBlockValidationGroup;
import com.dj.ckw.pageservice.enums.BlockType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BlockRequest {
    @NotNull(groups = {CreateBlockValidationGroup.class})
    private UUID parentId;
    @NotNull(groups = {CreateBlockValidationGroup.class})
    private BlockType type;
    @NotNull
    private String contentJson;
    @NotNull(groups = {CreateBlockValidationGroup.class})
    private Integer orderIndex;
}
