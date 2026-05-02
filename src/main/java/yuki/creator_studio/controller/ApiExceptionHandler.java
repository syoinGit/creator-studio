package yuki.creator_studio.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import yuki.creator_studio.exception.ResourceNotFoundException;
import yuki.creator_studio.exception.StorageOperationException;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(Map.of("message", e.getMessage()));
  }

  @ExceptionHandler(StorageOperationException.class)
  public ResponseEntity<Map<String, String>> handleStorage(StorageOperationException e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("message", e.getMessage()));
  }
}
