package com.dj.ckw.workspaceservice.service.impl;

import com.dj.ckw.workspaceservice.dto.*;
import com.dj.ckw.workspaceservice.enums.WorkspaceMemberRole;
import com.dj.ckw.workspaceservice.exception.ConflictException;
import com.dj.ckw.workspaceservice.exception.NotFoundException;
import com.dj.ckw.workspaceservice.exception.UnauthorizedException;
import com.dj.ckw.workspaceservice.model.Workspace;
import com.dj.ckw.workspaceservice.model.WorkspaceMember;
import com.dj.ckw.workspaceservice.repository.WorkspaceMemberRepository;
import com.dj.ckw.workspaceservice.repository.WorkspaceRepository;
import com.dj.ckw.workspaceservice.service.WorkspaceService;
import com.dj.ckw.workspaceservice.utils.WorkspaceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

  private static final Logger log = LoggerFactory.getLogger(WorkspaceServiceImpl.class);

  private final WorkspaceRepository workspaceRepository;
  private final WorkspaceMemberRepository workspaceMemberRepository;

  protected WorkspaceServiceImpl() {
    this.workspaceRepository = null;
    this.workspaceMemberRepository = null;
  }

  public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository,
      WorkspaceMemberRepository workspaceMemberRepository) {
    this.workspaceRepository = workspaceRepository;
    this.workspaceMemberRepository = workspaceMemberRepository;
  }

  @Transactional
  public WorkspaceResponse create(CreateWorkspaceRequest req, String ownerId) {
    log.info("Creating workspace '{}' for owner '{}'", req.getName(), ownerId);
    if (workspaceRepository.existsByNameAndOwnerId(req.getName(), ownerId)) {
      throw new ConflictException("Workspace with the same name already exists for this owner");
    }
    Workspace w = WorkspaceMapper.toEntity(req, ownerId);
    Workspace saved = workspaceRepository.save(w);

    WorkspaceMember ownerMember = WorkspaceMember.builder()
        .workspace(w)
        .role(WorkspaceMemberRole.OWNER)
        .userId(ownerId)
        .build();
    workspaceMemberRepository.save(ownerMember);

    log.info("Workspace created: {}", saved.getId());
    return WorkspaceMapper.toDto(saved);
  }

  @Override
  public WorkspaceResponse getById(UUID id, String requesterId) {
    Workspace w = workspaceRepository.findById(id).orElseThrow(() -> new NotFoundException("Workspace not found"));
    // allow owner or members logic could go here; for now only owner can view
    if (!w.getOwnerId().equals(requesterId)) {
      throw new UnauthorizedException("Not allowed to access this workspace");
    }
    return WorkspaceMapper.toDto(w);
  }

  @Override
  public PagedResponse<WorkspaceResponse> listByUserPaged(String ownerId, int page, int size, String search) {
    // Convert 1-based page to 0-based
    int clientPage = Math.max(1, page);
    int effectivePage = clientPage - 1;
    int effectiveSize = size > 0 ? size : 20;

    PageRequest pr = PageRequest.of(effectivePage, effectiveSize);
    Page<Workspace> p;
    if (search != null && !search.isBlank()) {
      p = workspaceRepository.findAllByOwnerIdAndNameContainingIgnoreCase(ownerId, search, pr);
    } else {
      p = workspaceRepository.findAllByOwnerId(ownerId, pr);
    }

    List<WorkspaceResponse> content = p.getContent().stream().map(WorkspaceMapper::toDto).collect(Collectors.toList());
    return new PagedResponse<>(content, p.getTotalElements(), p.getTotalPages(), clientPage, p.getSize());
  }

  @Override
  public WorkspaceMembersResponseDto getWorkspaceMembers(UUID workspaceId, String requesterId) {
    Workspace w = workspaceRepository.findById(workspaceId)
        .orElseThrow(() -> new NotFoundException("Workspace not found"));
    WorkspaceMember workspaceMember = workspaceMemberRepository.findByUserIdAndWorkspace(requesterId, w);
    if (workspaceMember == null || !workspaceMember.getWorkspace().getId().equals(workspaceId)) {
      throw new UnauthorizedException("Not allowed to access members of this workspace");
    }
    return new WorkspaceMembersResponseDto(workspaceMemberRepository.findByWorkspace(w));
  }

  @Override
  @Transactional
  public void transferOwnership(UUID workspaceId, String newOwnerId, String requesterId) {
    log.info("Transferring ownership of workspace '{}' to '{}'", workspaceId, newOwnerId);
    Workspace w = workspaceRepository.findById(workspaceId)
        .orElseThrow(() -> new NotFoundException("Workspace not found"));
    WorkspaceMember newOwnerMember = workspaceMemberRepository.findByUserIdAndWorkspace(newOwnerId, w);
    WorkspaceMember currentOwnerMember = workspaceMemberRepository.findByUserIdAndWorkspace(requesterId, w);
    if (!w.getOwnerId().equals(requesterId)) {
      throw new UnauthorizedException("Not allowed to transfer ownership of this workspace");
    }
    if (newOwnerMember == null || !newOwnerMember.getWorkspace().getId().equals(workspaceId)) {
      throw new ConflictException("New owner must be a member of the workspace");
    }
    if (currentOwnerMember == null || !currentOwnerMember.getWorkspace().getId().equals(workspaceId)) {
      throw new ConflictException("Current owner must be a member of the workspace");
    }
    w.setOwnerId(newOwnerId);
    w.setUpdatedAt(new java.util.Date());
    newOwnerMember.setRole(WorkspaceMemberRole.OWNER);
    currentOwnerMember.setRole(WorkspaceMemberRole.EDITOR);
    workspaceRepository.save(w);
    workspaceMemberRepository.save(newOwnerMember);
    workspaceMemberRepository.save(currentOwnerMember);
    log.info("Ownership transferred successfully");
  }

  @Override
  public WorkspaceResponse update(UUID id, UpdateWorkspaceRequest req, String requesterId) {
    log.info("Updating workspace '{}'", id);
    Workspace w = workspaceRepository.findById(id).orElseThrow(() -> new NotFoundException("Workspace not found"));
    if (!w.getOwnerId().equals(requesterId)) {
      throw new UnauthorizedException("Not allowed to update this workspace");
    }
    if (req.getName() != null && !req.getName().isBlank() && !req.getName().equals(w.getName())) {
      if (workspaceRepository.existsByNameAndOwnerId(req.getName(), requesterId)) {
        throw new ConflictException("Workspace with the same name already exists for this owner");
      }
      w.setName(req.getName());
    }
    if (req.getDescription() != null) {
      w.setDescription(req.getDescription());
    }
    w.setUpdatedAt(new java.util.Date());
    Workspace saved = workspaceRepository.save(w);
    log.info("Workspace updated: {}", saved.getId());
    return WorkspaceMapper.toDto(saved);
  }

  @Override
  public void delete(UUID id, String requesterId) {
    log.info("Deleting workspace '{}'", id);
    Workspace w = workspaceRepository.findById(id).orElseThrow(() -> new NotFoundException("Workspace not found"));
    if (!w.getOwnerId().equals(requesterId)) {
      throw new UnauthorizedException("Not allowed to delete this workspace");
    }
    workspaceRepository.delete(w);
    log.info("Workspace deleted: {}", id);
  }

  public Optional<WorkspaceMemberRole> getWorkspaceMemberRole(String workspaceId, String requesterId) {
    Workspace w = workspaceRepository.findById(UUID.fromString(workspaceId))
        .orElseThrow(() -> new NotFoundException("Workspace not found"));

    WorkspaceMember workspaceMember = workspaceMemberRepository.findByUserIdAndWorkspace(requesterId, w);

    return Optional.ofNullable(workspaceMember).map(WorkspaceMember::getRole);

  }
}
