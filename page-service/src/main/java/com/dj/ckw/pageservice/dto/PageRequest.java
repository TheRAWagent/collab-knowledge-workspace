package com.dj.ckw.pageservice.dto;

import com.dj.ckw.pageservice.dto.validation.CreatePageValidationGroup;
import com.dj.ckw.pageservice.dto.validation.UpdatePageValidationGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PageRequest {
    @NotNull(groups = {CreatePageValidationGroup.class} , message = "Title is required")
    @NotBlank(message = "title cannot be empty")
    private String title;
    @NotBlank(groups = {UpdatePageValidationGroup.class})
    private String icon;
}
