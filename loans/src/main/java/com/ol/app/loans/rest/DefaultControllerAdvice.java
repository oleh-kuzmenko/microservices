package com.ol.app.loans.rest;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
class DefaultControllerAdvice {

  private static final String DEFAULT_ERROR_LOG = "Error processing request: {}";

  @ExceptionHandler(Exception.class)
  ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .body(ErrorResponse.builder()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .path(path)
            .error("Internal server error")
            .build());
  }

  @ExceptionHandler(ResponseStatusException.class)
  ResponseEntity<ErrorResponse> handleResponseStatusException(HttpServletRequest request, ResponseStatusException e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(e.getStatusCode())
        .body(ErrorResponse.builder()
            .timestamp(OffsetDateTime.now())
            .status(e.getStatusCode().value())
            .path(path)
            .error(e.getReason())
            .build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
        .body(ErrorResponse.builder()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .path(path)
            .errors(e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList())
            .build());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ErrorResponse> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
        .body(ErrorResponse.builder()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .path(path)
            .error(e.getMessage())
            .build());
  }

  private String getErrorPath(HttpServletRequest request) {
    return request.getServletPath();
  }
  
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record ErrorResponse(OffsetDateTime timestamp, Integer status, String path, String error, List<String> errors) {
  }
}
