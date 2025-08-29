package com.example.travelshop.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
      Map<String,String> errors = ex.getBindingResult().getFieldErrors().stream()
         .collect(Collectors.toMap(f -> f.getField(), f -> f.getDefaultMessage()));
      return ResponseEntity.badRequest().body(errors);
   }

   @ExceptionHandler(RuntimeException.class)
   public ResponseEntity<String> handleRuntime(RuntimeException ex) {
      return ResponseEntity
         .status(500)
         .body(ex.getMessage());
   }
}