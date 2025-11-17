package com.dj.ckw.workspaceservice.dto;

import jakarta.validation.constraints.Size;

public class UpdateWorkspaceRequest {

    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    public UpdateWorkspaceRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

