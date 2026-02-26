package com.dj.ckw.pageservice.dto;

import java.util.UUID;

/**
 * Request body for moving a document to a different directory (or root).
 */
public class MovePageRequest {

  /** null = move to root level */
  private UUID directoryId;

  private Integer sortOrder;

  public MovePageRequest() {
  }

  public UUID getDirectoryId() {
    return directoryId;
  }

  public void setDirectoryId(UUID directoryId) {
    this.directoryId = directoryId;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }
}
