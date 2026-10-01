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

**The current upload has Java files at the repository root.** Before running Maven, organize your local copy into the package directories below. Create these directories and move each listed file there; keep `pom.xml` at the root.

| Destination | Files |
| --- | --- |
| `src/main/java/com/student/notes/` | `NotesManagerApplication.java` |
| `src/main/java/com/student/notes/controller/` | `NoteController.java` |
| `src/main/java/com/student/notes/dto/` | `ApiResponseDTO.java`, `NoteRequestDTO.java` |
| `src/main/java/com/student/notes/model/` | `Note.java` |
| `src/main/java/com/student/notes/repository/` | `NoteRepository.java` |
| `src/main/java/com/student/notes/service/` | `NoteService.java`, `NoteServiceImpl.java` |
| `src/main/java/com/student/notes/exception/` | `NoteNotFoundException.java`, `GlobalExceptionHandler.java` |
| `src/main/java/com/student/notes/config/` | `DataInitializer.java` |
| `src/main/resources/` | `application.properties` |
| `src/test/java/com/student/notes/` | `NotesManagerApplicationTests.java` |

Then run:

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

A JUnit/Spring Boot test class is included. After arranging the local source tree, run `mvn test`. This README documents the source; it does not claim that tests have been executed.

This is a learning project without authentication or durable persistence. A clean Maven source layout and persistent storage are useful next improvements.
