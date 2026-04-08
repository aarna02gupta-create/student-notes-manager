package com.student.notes.dto;

import java.time.LocalDateTime;

/**
 * Generic API Response wrapper using Java Generics.
 * Wraps every response from the REST API in a consistent
 * envelope containing status, message, and data payload.
 *
 * @param <T> the type of the data payload
 */
public class ApiResponseDTO<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    // ─── Constructors ──────────────────────────────────────────────────────────

    public ApiResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponseDTO(boolean success, String message, T data) {
        this.success   = success;
        this.message   = message;
        this.data      = data;
        this.timestamp = LocalDateTime.now();
    }

    // ─── Static factory helpers ────────────────────────────────────────────────

    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>(true, message, data);
    }

    public static <T> ApiResponseDTO<T> error(String message) {
        return new ApiResponseDTO<>(false, message, null);
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public boolean isSuccess()                    { return success; }
    public void setSuccess(boolean success)       { this.success = success; }

    public String getMessage()                    { return message; }
    public void setMessage(String message)        { this.message = message; }

    public T getData()                            { return data; }
    public void setData(T data)                   { this.data = data; }

    public LocalDateTime getTimestamp()                      { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp)        { this.timestamp = timestamp; }
}
