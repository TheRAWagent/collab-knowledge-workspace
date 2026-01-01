package com.dj.ckw.pageservice.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BlockDto {
    @NotNull
    private String type;
    @ArraySchema(schema = @Schema(ref = "#/components/schemas/BlockDto"))
    private List<BlockDto> content;
    @Schema(type = "object")
    private JsonNode attrs;
    @Schema(type = "object")
    private JsonNode marks;
    @Nullable
    private String text;
}

