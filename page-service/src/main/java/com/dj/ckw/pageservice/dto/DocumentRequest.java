package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.dto.validation.CreateDocumentValidationGroup;
import com.dj.ckw.pageservice.dto.validation.UpdateDocumentValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DocumentRequest {
    @NotNull(groups = {CreateDocumentValidationGroup.class} , message = "Title is required")
    @NotBlank(message = "title cannot be empty")
    private String title;
    @NotBlank(groups = {UpdateDocumentValidationGroup.class})
    private String icon;

    public DocumentRequest() {
    }

    public DocumentRequest(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
