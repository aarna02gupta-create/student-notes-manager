# Student Notes Manager

A Spring Boot REST API for creating, organizing, and searching student study notes. The source demonstrates a controller/service/repository structure, validated request DTOs, and centralized error handling.

## Features

- Create, list, retrieve, update, and delete notes.
- Filter by student name or subject, and retrieve the total note count.
- Validate title, subject, content, and student name with Bean Validation.
- Return a consistent response envelope with success, message, data, and timestamp.
- Store notes in memory using `ConcurrentHashMap` and generate IDs using `AtomicLong`.
- Load sample notes at startup.

## Stack and architecture

**Java 17 · Spring Boot 3.2.4 · Maven · Spring Web · Jakarta Validation**

Requests pass through `NoteController` → `NoteServiceImpl` → `NoteRepository`. Request DTOs separate input validation from the note model; `GlobalExceptionHandler` handles missing notes and invalid requests.

This version uses in-memory storage. Changes are lost when the application restarts; no database is required.

## Run locally

Install JDK 17 and Maven, then clone:

```bash
git clone https://github.com/aarna02gupta-create/student-notes-manager.git
cd student-notes-manager
```

The source follows the standard Maven layout: `src/main/java`, `src/main/resources`, and `src/test/java`. No file rearrangement is needed.

Run:

```bash
mvn spring-boot:run
```

The configured API address is `http://localhost:8080/api/notes`.

## Endpoints

| Method | Route | Purpose |
| --- | --- | --- |
| POST | `/api/notes` | Create a note |
| GET | `/api/notes` | List notes |
| GET | `/api/notes/{id}` | Retrieve one note |
| GET | `/api/notes/student?name=Aarna` | Filter by student |
| GET | `/api/notes/subject?name=DBMS` | Filter by subject |
| GET | `/api/notes/count` | Count notes |
| PUT | `/api/notes/{id}` | Update a note |
| DELETE | `/api/notes/{id}` | Delete a note |

## Example request

In Postman, send a **POST** request to `http://localhost:8080/api/notes`, select **Body → raw → JSON**, and use:

```json
{
  "title": "Normalization",
  "subject": "DBMS",
  "content": "Revision notes covering 3NF and BCNF.",
  "studentName": "Aarna"
}
```

A successful create returns HTTP 201 and the saved note in the response's `data` field. Use its returned ID for update, read, or delete requests.

## Tests and limitations

Run `mvn test` to execute the included JUnit 5 service tests. They construct a fresh in-memory repository for each test; they do not exercise HTTP routing or request validation.

This is a learning project without authentication or durable persistence. Useful next improvements are HTTP-level tests for valid/invalid requests and missing notes, followed by relational persistence when needed.
