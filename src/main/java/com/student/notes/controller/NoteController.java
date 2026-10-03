package com.student.notes.controller;

import com.student.notes.dto.ApiResponseDTO;
import com.student.notes.dto.NoteRequestDTO;
import com.student.notes.model.Note;
import com.student.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Student Notes Manager.
 *
 * Exposes RESTful CRUD endpoints under /api/notes.
 *
 * Advanced Java Concepts used:
 * - @RestController: Combines @Controller + @ResponseBody (Spring MVC)
 * - @RequestMapping: Maps HTTP routes to handler methods
 * - ResponseEntity<T>: Full HTTP response control (status, headers, body)
 * - @Valid: Triggers Bean Validation on request bodies
 * - @PathVariable: Extracts URI path parameters
 * - @RequestParam: Extracts query string parameters
 * - Generics: Used in ResponseEntity<ApiResponseDTO<T>>
 *
 * ─── Endpoints ────────────────────────────────────────────────
 * POST   /api/notes              → Create a new note
 * GET    /api/notes              → Get all notes
 * GET    /api/notes/{id}         → Get note by ID
 * GET    /api/notes/student?name → Get notes by student name
 * GET    /api/notes/subject?name → Get notes by subject
 * GET    /api/notes/count        → Get total note count
 * PUT    /api/notes/{id}         → Update a note by ID
 * DELETE /api/notes/{id}         → Delete a note by ID
 * ──────────────────────────────────────────────────────────────
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    /**
     * Constructor-based Dependency Injection.
     * Spring injects NoteServiceImpl at runtime.
     *
     * @param noteService the service layer bean
     */
    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // CREATE — POST /api/notes
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Creates a new Student Note.
     *
     * @param dto validated request body containing note details
     * @return 201 Created with the saved Note object
     */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Note>> createNote(@Valid @RequestBody NoteRequestDTO dto) {
        Note created = noteService.createNote(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Note created successfully.", created));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // READ ALL — GET /api/notes
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves all notes stored in the system.
     *
     * @return 200 OK with a list of all notes
     */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<Note>>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();

        String message = notes.isEmpty()
                ? "No notes found."
                : "Fetched " + notes.size() + " note(s) successfully.";

        return ResponseEntity.ok(ApiResponseDTO.success(message, notes));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // READ BY ID — GET /api/notes/{id}
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves a specific note by its unique ID.
     *
     * @param id the note ID from the URL path
     * @return 200 OK with the note, or 404 if not found (via GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Note>> getNoteById(@PathVariable Long id) {
        Note note = noteService.getNoteById(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Note found.", note));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // READ BY STUDENT — GET /api/notes/student?name=John
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves all notes belonging to a specific student.
     *
     * @param name the student's name as a query parameter
     * @return 200 OK with the filtered notes list
     */
    @GetMapping("/student")
    public ResponseEntity<ApiResponseDTO<List<Note>>> getNotesByStudent(
            @RequestParam("name") String name) {

        List<Note> notes = noteService.getNotesByStudent(name);

        String message = notes.isEmpty()
                ? "No notes found for student: " + name
                : "Found " + notes.size() + " note(s) for student: " + name;

        return ResponseEntity.ok(ApiResponseDTO.success(message, notes));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // READ BY SUBJECT — GET /api/notes/subject?name=Mathematics
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Retrieves all notes for a specific subject.
     *
     * @param name the subject name as a query parameter
     * @return 200 OK with the filtered notes list
     */
    @GetMapping("/subject")
    public ResponseEntity<ApiResponseDTO<List<Note>>> getNotesBySubject(
            @RequestParam("name") String name) {

        List<Note> notes = noteService.getNotesBySubject(name);

        String message = notes.isEmpty()
                ? "No notes found for subject: " + name
                : "Found " + notes.size() + " note(s) for subject: " + name;

        return ResponseEntity.ok(ApiResponseDTO.success(message, notes));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // COUNT — GET /api/notes/count
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Returns the total number of notes currently stored.
     *
     * @return 200 OK with the count value
     */
    @GetMapping("/count")
    public ResponseEntity<ApiResponseDTO<Map<String, Long>>> getCount() {
        long count = noteService.getTotalCount();
        Map<String, Long> payload = Map.of("totalNotes", count);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Total notes count fetched.", payload));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // UPDATE — PUT /api/notes/{id}
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Updates an existing note with new data.
     *
     * @param id  the note ID from the URL path
     * @param dto validated request body with updated note details
     * @return 200 OK with the updated Note, or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Note>> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteRequestDTO dto) {

        Note updated = noteService.updateNote(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.success("Note updated successfully.", updated));
    }

    // ──────────────────────────────────────────────────────────────────────────
    // DELETE — DELETE /api/notes/{id}
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Deletes a specific note by its ID.
     *
     * @param id the note ID from the URL path
     * @return 200 OK with confirmation message, or 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success("Note with ID " + id + " deleted successfully.", null));
    }
}
