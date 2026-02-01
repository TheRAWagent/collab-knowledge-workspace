package com.dj.ckw.workspaceservice.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest req) {
    log.warn("Not found: {}", ex.getMessage());
    ErrorResponse er = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage(),
        req.getRequestURI());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(er);
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest req) {
    log.warn("Unauthorized: {}", ex.getMessage());
    ErrorResponse er = new ErrorResponse(Instant.now(), HttpStatus.FORBIDDEN.value(), "Forbidden", ex.getMessage(),
        req.getRequestURI());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(er);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest req) {
    log.warn("Conflict: {}", ex.getMessage());
    ErrorResponse er = new ErrorResponse(Instant.now(), HttpStatus.CONFLICT.value(), "Conflict", ex.getMessage(),
        req.getRequestURI());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(er);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest req) {
    log.warn("Bad Request: {}", ex.getMessage());
    ErrorResponse er = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Bad Request", ex.getMessage(),
        req.getRequestURI());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
    Map<String, String> fieldErrors = new HashMap<>();
    for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
      fieldErrors.put(fe.getField(), fe.getDefaultMessage());
    }
    log.warn("Validation failed: {}", fieldErrors);
    ErrorResponse er = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Validation Failed",
        "Validation errors", req.getRequestURI());
    er.setValidationErrors(fieldErrors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(er);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
    log.error("Unexpected error", ex);
    ErrorResponse er = new ErrorResponse(Instant.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Internal Server Error", ex.getMessage(), req.getRequestURI());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(er);
  }
}
