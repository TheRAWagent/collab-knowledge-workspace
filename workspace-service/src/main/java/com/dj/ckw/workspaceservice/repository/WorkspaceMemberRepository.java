package com.dj.ckw.workspaceservice.repository;

import com.dj.ckw.workspaceservice.model.Workspace;
import com.dj.ckw.workspaceservice.model.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, UUID> {
    List<WorkspaceMember> findByWorkspace(Workspace workspace);

    WorkspaceMember findByUserIdAndWorkspace(String userId, Workspace workspace);
}
