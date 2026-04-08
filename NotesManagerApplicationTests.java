package com.student.notes;

import com.student.notes.dto.NoteRequestDTO;
import com.student.notes.exception.NoteNotFoundException;
import com.student.notes.model.Note;
import com.student.notes.repository.NoteRepository;
import com.student.notes.service.NoteService;
import com.student.notes.service.NoteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for the Student Notes Manager Service Layer.
 *
 * Tests all CRUD operations on the in-memory store.
 * Uses JUnit 5 (Jupiter) — part of Spring Boot Test starter.
 *
 * Advanced Java Concepts tested:
 * - Custom exception throwing
 * - Optional handling
 * - Stream filtering
 * - In-memory CRUD correctness
 */
@DisplayName("Student Notes Manager — Service Unit Tests")
class NotesManagerApplicationTests {

    private NoteService noteService;

    /**
     * Re-creates a fresh in-memory store before every test.
     * This ensures tests are isolated and do not share state.
     */
    @BeforeEach
    void setUp() {
        NoteRepository freshRepo = new NoteRepository();
        noteService = new NoteServiceImpl(freshRepo);
    }

    // ─── CREATE Tests ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("CREATE: Should create a note and assign an ID")
    void testCreateNote() {
        NoteRequestDTO dto = new NoteRequestDTO(
                "Java Basics", "CS", "Content here", "Alice");

        Note created = noteService.createNote(dto);

        assertNotNull(created.getId(), "ID should be assigned after creation");
        assertEquals("Java Basics", created.getTitle());
        assertEquals("CS",          created.getSubject());
        assertEquals("Alice",        created.getStudentName());
    }

    @Test
    @DisplayName("CREATE: Multiple notes should have unique IDs")
    void testMultipleNotesHaveUniqueIds() {
        NoteRequestDTO dto1 = new NoteRequestDTO("Note 1", "Math", "Content 1", "Bob");
        NoteRequestDTO dto2 = new NoteRequestDTO("Note 2", "Science", "Content 2", "Carol");

        Note n1 = noteService.createNote(dto1);
        Note n2 = noteService.createNote(dto2);

        assertNotEquals(n1.getId(), n2.getId(), "Each note must have a unique ID");
    }

    // ─── READ Tests ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("READ: Get all notes should return all created notes")
    void testGetAllNotes() {
        noteService.createNote(new NoteRequestDTO("T1", "S1", "C1", "Student1"));
        noteService.createNote(new NoteRequestDTO("T2", "S2", "C2", "Student2"));

        List<Note> all = noteService.getAllNotes();

        assertEquals(2, all.size(), "Should return exactly 2 notes");
    }

    @Test
    @DisplayName("READ: Get note by valid ID should return correct note")
    void testGetNoteById_Found() {
        Note created = noteService.createNote(
                new NoteRequestDTO("Physics Note", "Physics", "F=ma", "Bob"));

        Note fetched = noteService.getNoteById(created.getId());

        assertEquals(created.getId(), fetched.getId());
        assertEquals("Physics Note", fetched.getTitle());
    }

    @Test
    @DisplayName("READ: Get note by invalid ID should throw NoteNotFoundException")
    void testGetNoteById_NotFound() {
        assertThrows(NoteNotFoundException.class,
                () -> noteService.getNoteById(999L),
                "Should throw NoteNotFoundException for non-existent ID");
    }

    @Test
    @DisplayName("READ: Filter notes by student name")
    void testGetNotesByStudent() {
        noteService.createNote(new NoteRequestDTO("Note A", "Math",    "Content", "Alice"));
        noteService.createNote(new NoteRequestDTO("Note B", "Science", "Content", "Alice"));
        noteService.createNote(new NoteRequestDTO("Note C", "History", "Content", "Bob"));

        List<Note> aliceNotes = noteService.getNotesByStudent("Alice");

        assertEquals(2, aliceNotes.size(), "Alice should have 2 notes");
        assertTrue(aliceNotes.stream()
                             .allMatch(n -> n.getStudentName().equalsIgnoreCase("Alice")));
    }

    @Test
    @DisplayName("READ: Filter notes by subject")
    void testGetNotesBySubject() {
        noteService.createNote(new NoteRequestDTO("Note A", "Math",    "Content", "Alice"));
        noteService.createNote(new NoteRequestDTO("Note B", "Math",    "Content", "Bob"));
        noteService.createNote(new NoteRequestDTO("Note C", "Science", "Content", "Carol"));

        List<Note> mathNotes = noteService.getNotesBySubject("Math");

        assertEquals(2, mathNotes.size(), "Should find 2 notes for Math");
    }

    // ─── UPDATE Tests ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("UPDATE: Should update note fields correctly")
    void testUpdateNote() {
        Note original = noteService.createNote(
                new NoteRequestDTO("Old Title", "Old Subject", "Old Content", "Dave"));

        NoteRequestDTO updateDto = new NoteRequestDTO(
                "New Title", "New Subject", "New Content", "Dave");

        Note updated = noteService.updateNote(original.getId(), updateDto);

        assertEquals(original.getId(), updated.getId(), "ID must remain the same after update");
        assertEquals("New Title",   updated.getTitle());
        assertEquals("New Subject", updated.getSubject());
        assertEquals("New Content", updated.getContent());
    }

    @Test
    @DisplayName("UPDATE: Updating non-existent note should throw NoteNotFoundException")
    void testUpdateNote_NotFound() {
        NoteRequestDTO dto = new NoteRequestDTO("T", "S", "C", "Student");
        assertThrows(NoteNotFoundException.class,
                () -> noteService.updateNote(999L, dto));
    }

    // ─── DELETE Tests ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE: Should delete a note successfully")
    void testDeleteNote() {
        Note created = noteService.createNote(
                new NoteRequestDTO("To Delete", "Subject", "Content", "Eve"));

        noteService.deleteNote(created.getId());

        assertThrows(NoteNotFoundException.class,
                () -> noteService.getNoteById(created.getId()),
                "Deleted note should no longer be found");
    }

    @Test
    @DisplayName("DELETE: Deleting non-existent note should throw NoteNotFoundException")
    void testDeleteNote_NotFound() {
        assertThrows(NoteNotFoundException.class,
                () -> noteService.deleteNote(999L));
    }

    // ─── COUNT Tests ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("COUNT: Should return correct number of notes")
    void testGetTotalCount() {
        assertEquals(0, noteService.getTotalCount(), "Initially zero notes");

        noteService.createNote(new NoteRequestDTO("N1", "S1", "C1", "F1"));
        noteService.createNote(new NoteRequestDTO("N2", "S2", "C2", "F2"));

        assertEquals(2, noteService.getTotalCount(), "Should report 2 after two creates");
    }
}
