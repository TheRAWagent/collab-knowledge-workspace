package com.dj.ckw.pageservice.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.List;

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

    public BlockDto() {
    }

    public BlockDto(String type, List<BlockDto> content, JsonNode attrs, JsonNode marks, String text) {
        this.type = type;
        this.content = content;
        this.attrs = attrs;
        this.marks = marks;
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BlockDto> getContent() {
        return content;
    }

    public void setContent(List<BlockDto> content) {
        this.content = content;
    }

    public JsonNode getAttrs() {
        return attrs;
    }

    public void setAttrs(JsonNode attrs) {
        this.attrs = attrs;
    }

    public JsonNode getMarks() {
        return marks;
    }

    public void setMarks(JsonNode marks) {
        this.marks = marks;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

