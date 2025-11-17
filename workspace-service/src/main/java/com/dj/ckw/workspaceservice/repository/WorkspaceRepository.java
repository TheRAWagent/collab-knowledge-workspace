package com.dj.ckw.workspaceservice.repository;

import com.dj.ckw.workspaceservice.model.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    Optional<Workspace> findByIdAndOwnerId(UUID id, String ownerId);
    List<Workspace> findAllByOwnerId(String ownerId);
    boolean existsByNameAndOwnerId(String name, String ownerId);

    // Pageable methods
    Page<Workspace> findAllByOwnerId(String ownerId, Pageable pageable);
    Page<Workspace> findAllByOwnerIdAndNameContainingIgnoreCase(String ownerId, String name, Pageable pageable);
}
