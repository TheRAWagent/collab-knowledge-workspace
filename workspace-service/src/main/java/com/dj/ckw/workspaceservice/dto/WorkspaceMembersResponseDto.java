package com.dj.ckw.workspaceservice.dto;

import com.dj.ckw.workspaceservice.model.WorkspaceMember;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorkspaceMembersResponseDto {
    private List<WorkspaceMember> members;
}
