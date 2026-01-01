package com.dj.ckw.pageservice.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "blocks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockEntity {
    @Id
    private UUID id;

    @Column("document_id")
    @NotNull
    private UUID documentId;

    @Column("parent_id")
    private UUID parentId;   // null = root-level block

    @Column("order_index")
    private Long orderIndex;

    @Column("type")
    private String type;

    private JsonNode attrs; // additional attributes as JSON

    @Column("text")
    private String text;

    @Column("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
