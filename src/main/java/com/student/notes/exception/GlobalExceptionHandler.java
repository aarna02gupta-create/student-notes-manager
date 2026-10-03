package com.student.notes.exception;

import com.student.notes.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler using @RestControllerAdvice.
 *
 * Advanced Java Concept: AOP-based global exception handling via @ControllerAdvice.
 * Intercepts exceptions thrown from any @RestController and returns structured responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NoteNotFoundException — returns 404 Not Found.
     */
    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNoteNotFound(NoteNotFoundException ex) {
        ApiResponseDTO<Void> response = ApiResponseDTO.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handles validation failures from @Valid — returns 400 Bad Request
     * with field-level error messages.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiResponseDTO<Map<String, String>> response =
                new ApiResponseDTO<>(false, "Validation failed. Please check your input.", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handles all unexpected exceptions — returns 500 Internal Server Error.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGenericException(Exception ex) {
        ApiResponseDTO<Void> response = ApiResponseDTO.error(
                "An unexpected error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
