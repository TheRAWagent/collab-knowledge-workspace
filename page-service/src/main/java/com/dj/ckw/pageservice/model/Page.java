package com.dj.ckw.pageservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "pages")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Page {
    @Id
    @Generated
    private UUID id;

    private UUID workspaceId;

    private String title;

    private String icon;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
