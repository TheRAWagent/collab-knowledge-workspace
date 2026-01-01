package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.dto.validation.CreateDocumentValidationGroup;
import com.dj.ckw.pageservice.dto.validation.UpdateDocumentValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentRequest {
    @NotNull(groups = {CreateDocumentValidationGroup.class} , message = "Title is required")
    @NotBlank(message = "title cannot be empty")
    private String title;
    @NotBlank(groups = {UpdateDocumentValidationGroup.class})
    private String icon;
}
