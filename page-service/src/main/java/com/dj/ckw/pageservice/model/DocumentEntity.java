package com.dj.ckw.pageservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "documents")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentEntity {
    @Id
    private UUID id;

    private UUID workspaceId;

    private String title;

    private String icon;

    /**
     * Optimistic version
     */
    private int version;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
