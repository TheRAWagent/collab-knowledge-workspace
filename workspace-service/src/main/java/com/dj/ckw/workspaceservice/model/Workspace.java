package com.dj.ckw.workspaceservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workspace")
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "owner_id", nullable = false)
    private String ownerId;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name is required")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;

    // OneToMany relation to WorkspaceMember with cascade delete and orphanRemoval
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkspaceMember> members = new ArrayList<>();

    public Workspace() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<WorkspaceMember> getMembers() {
        return members;
    }

    public void setMembers(List<WorkspaceMember> members) {
        this.members = members;
    }

    // Convenience helpers to manage bidirectional relationship
    public void addMember(WorkspaceMember member) {
        if (member == null) return;
        members.add(member);
        member.setWorkspace(this);
    }

    public void removeMember(WorkspaceMember member) {
        if (member == null) return;
        members.remove(member);
        member.setWorkspace(null);
    }
}
