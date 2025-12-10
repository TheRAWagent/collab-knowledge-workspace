package com.dj.ckw.pageservice.model;

import com.dj.ckw.pageservice.enums.BlockType;
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
public class Block {
    @Id
    private UUID id;

    @Column("page_id")
    @NotNull
    private UUID pageId;

    @Column("parent_id")
    private UUID parentId;   // null = root-level block

    private BlockType type;

    @Column("content_json")
    private String contentJson; // rich JSON content (TipTap style)

    @Column("order_index")
    private Integer orderIndex;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
