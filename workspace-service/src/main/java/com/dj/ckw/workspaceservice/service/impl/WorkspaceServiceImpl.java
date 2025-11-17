package com.dj.ckw.workspaceservice.service.impl;

import com.dj.ckw.workspaceservice.dto.CreateWorkspaceRequest;
import com.dj.ckw.workspaceservice.dto.PagedResponse;
import com.dj.ckw.workspaceservice.dto.UpdateWorkspaceRequest;
import com.dj.ckw.workspaceservice.dto.WorkspaceResponse;
import com.dj.ckw.workspaceservice.exception.ConflictException;
import com.dj.ckw.workspaceservice.exception.NotFoundException;
import com.dj.ckw.workspaceservice.exception.UnauthorizedException;
import com.dj.ckw.workspaceservice.model.Workspace;
import com.dj.ckw.workspaceservice.repository.WorkspaceRepository;
import com.dj.ckw.workspaceservice.service.WorkspaceService;
import com.dj.ckw.workspaceservice.utils.WorkspaceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository repo;

    public WorkspaceServiceImpl(WorkspaceRepository repo) {
        this.repo = repo;
    }

    @Override
    public WorkspaceResponse create(CreateWorkspaceRequest req, String ownerId) {
        if (repo.existsByNameAndOwnerId(req.getName(), ownerId)) {
            throw new ConflictException("Workspace with the same name already exists for this owner");
        }
        Workspace w = WorkspaceMapper.toEntity(req, ownerId);
        Workspace saved = repo.save(w);
        return WorkspaceMapper.toDto(saved);
    }

    @Override
    public WorkspaceResponse getById(UUID id, String requesterId) {
        Workspace w = repo.findById(id).orElseThrow(() -> new NotFoundException("Workspace not found"));
        // allow owner or members logic could go here; for now only owner can view
        if (!w.getOwnerId().equals(requesterId)) {
            throw new UnauthorizedException("Not allowed to access this workspace");
        }
        return WorkspaceMapper.toDto(w);
    }

    @Override
    public List<WorkspaceResponse> listByOwner(String ownerId) {
        return repo.findAllByOwnerId(ownerId).stream().map(WorkspaceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PagedResponse<WorkspaceResponse> listByOwnerPaged(String ownerId, int page, int size, String search) {
        // Convert 1-based page to 0-based
        int clientPage = Math.max(1, page);
        int effectivePage = clientPage - 1;
        int effectiveSize = size > 0 ? size : 20;

        PageRequest pr = PageRequest.of(effectivePage, effectiveSize);
        Page<Workspace> p;
        if (search != null && !search.isBlank()) {
            p = repo.findAllByOwnerIdAndNameContainingIgnoreCase(ownerId, search, pr);
        } else {
            p = repo.findAllByOwnerId(ownerId, pr);
        }

        List<WorkspaceResponse> content = p.getContent().stream().map(WorkspaceMapper::toDto).collect(Collectors.toList());
        return new PagedResponse<>(content, p.getTotalElements(), p.getTotalPages(), clientPage, p.getSize());
    }

    @Override
    public WorkspaceResponse update(UUID id, UpdateWorkspaceRequest req, String requesterId) {
        Workspace w = repo.findById(id).orElseThrow(() -> new NotFoundException("Workspace not found"));
        if (!w.getOwnerId().equals(requesterId)) {
            throw new UnauthorizedException("Not allowed to update this workspace");
        }
        if (req.getName() != null && !req.getName().isBlank() && !req.getName().equals(w.getName())) {
            if (repo.existsByNameAndOwnerId(req.getName(), requesterId)) {
                throw new ConflictException("Workspace with the same name already exists for this owner");
            }
            w.setName(req.getName());
        }
        if (req.getDescription() != null) {
            w.setDescription(req.getDescription());
        }
        w.setUpdatedAt(new java.util.Date());
        Workspace saved = repo.save(w);
        return WorkspaceMapper.toDto(saved);
    }

    @Override
    public void delete(UUID id, String requesterId) {
        Workspace w = repo.findById(id).orElseThrow(() -> new NotFoundException("Workspace not found"));
        if (!w.getOwnerId().equals(requesterId)) {
            throw new UnauthorizedException("Not allowed to delete this workspace");
        }
        repo.delete(w);
    }
}
