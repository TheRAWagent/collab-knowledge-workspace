package com.dj.ckw.workspaceservice.dto;

import com.dj.ckw.workspaceservice.model.WorkspaceMember;

import java.util.List;

public class WorkspaceMembersResponseDto {
    private List<WorkspaceMember> members;

    public WorkspaceMembersResponseDto(List<WorkspaceMember> members) {
        this.members = members;
    }

    public List<WorkspaceMember> getMembers() {
        return members;
    }

    public void setMembers(List<WorkspaceMember> members) {
        this.members = members;
    }
}
