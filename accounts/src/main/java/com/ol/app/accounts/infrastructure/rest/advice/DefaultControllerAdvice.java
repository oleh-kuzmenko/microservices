package com.ol.app.accounts.infrastructure.rest.advice;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.ol.app.accounts.application.exception.NotUniqueException;
import com.ol.app.accounts.dto.ErrorResponse;
import com.ol.app.accounts.application.exception.NotFountException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class DefaultControllerAdvice {

  private static final String DEFAULT_ERROR_LOG = "Error processing request: {}";

  @ExceptionHandler(Exception.class)
  ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .body(new ErrorResponse()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .path(path)
            .error("Internal server error"));
  }

  @ExceptionHandler(NotFountException.class)
  ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest request, NotFountException e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
        .body(new ErrorResponse()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .path(path)
            .error(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
        .body(new ErrorResponse()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .path(path)
            .error(e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> "Attribute '%s' error: %s".formatted(extractAttribute(error), error.getDefaultMessage()))
                .collect(Collectors.joining("; "))));
  }

  @ExceptionHandler(NotUniqueException.class)
  ResponseEntity<ErrorResponse> handleNotUniqueException(HttpServletRequest request, NotUniqueException e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
        .body(new ErrorResponse()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .path(path)
            .error(e.getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  ResponseEntity<ErrorResponse> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
        .body(new ErrorResponse()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .path(path)
            .error(e.getMessage()));
  }

  @ExceptionHandler(RequestNotPermitted.class)
  ResponseEntity<ErrorResponse> handleRequestNotPermitted(HttpServletRequest request, RequestNotPermitted e) {
    String path = getErrorPath(request);
    log.error(DEFAULT_ERROR_LOG, path, e);
    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS.value())
        .body(new ErrorResponse()
            .timestamp(OffsetDateTime.now())
            .status(HttpStatus.TOO_MANY_REQUESTS.value())
            .path(path)
            .error(e.getMessage()));
  }

  private String getErrorPath(HttpServletRequest request) {
    return request.getServletPath();
  }

  private String extractAttribute(ObjectError error) {
    return Optional.of(error)
        .stream()
        .map(DefaultMessageSourceResolvable::getArguments)
        .filter(Objects::nonNull)
        .flatMap(Arrays::stream)
        .filter(MessageSourceResolvable.class::isInstance)
        .map(MessageSourceResolvable.class::cast)
        .findFirst()
        .map(MessageSourceResolvable::getDefaultMessage)
        .orElse("");
  }
}
