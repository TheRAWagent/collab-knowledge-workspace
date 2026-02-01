package com.dj.ckw.pageservice.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(BlockNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleBlockNotFoundException(BlockNotFoundException ex) {
    log.warn("Block not found: {}", ex.getMessage());
    return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(DocumentNotFoundException.class)
  public ResponseEntity<Map<String, String>> handlePageNotFoundException(DocumentNotFoundException ex) {
    log.warn("Document not found: {}", ex.getMessage());
    return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
    log.warn("Validation failed: {}", ex.getBindingResult());
    return ResponseEntity.badRequest().body(Map.of(
        "error", "Validation failed",
        "details", ex.getBindingResult().toString()));
  }

  @ExceptionHandler(OptimisticLockingFailureException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public Mono<String> handleVersionConflict(Exception e) {
    log.warn("Optimistic locking failure: {}", e.getMessage());
    return Mono.just(e.getMessage());
  }

  @ExceptionHandler(SnapshotNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleSnapshotNotFoundException(SnapshotNotFoundException ex) {
    log.warn("Snapshot not found: {}", ex.getMessage());
    return ResponseEntity.status(404).body(Map.of("error", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception e) {
    log.error("Unexpected error occurred", e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("error", "An unexpected error occurred"));
  }

}
