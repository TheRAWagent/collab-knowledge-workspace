package com.dj.ckw.workspaceservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workspace")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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
  @Builder.Default
  @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<WorkspaceMember> members = new ArrayList<>();
}
