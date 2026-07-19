# Student Notes Manager

A RESTful backend for managing student study notes — built with Spring Boot using a clean layered architecture (Controller → Service → Repository), DTO-based validation, and centralized exception handling.

## Features
- Full CRUD on notes: create, read, update, delete
- Query notes by student name or by subject
- Live note count endpoint
- Request validation via Bean Validation (`@Valid`) with field-level error messages
- Centralized error handling via `@RestControllerAdvice` (404 for missing notes, 400 for validation failures, 500 fallback)
- Consistent response envelope (`ApiResponseDTO<T>`) wrapping every response with success flag, message, data, and timestamp
- Thread-safe in-memory storage (`ConcurrentHashMap` + `AtomicLong` ID generation) — no database setup required to run
- Seed data loaded automatically on startup via `CommandLineRunner`

## Tech Stack
- Java 17
- Spring Boot 3.2.4 (Web, Validation, DevTools)
- Maven

## Architecture
