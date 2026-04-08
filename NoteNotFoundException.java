package com.student.notes.exception;

/**
 * Custom Runtime Exception thrown when a requested Note is not found
 * in the in-memory store.
 *
 * Advanced Java Concept: Custom Exceptions extending RuntimeException,
 * which is part of Exception Handling — a core Advanced Java concept.
 */
public class NoteNotFoundException extends RuntimeException {

    private final Long noteId;

    public NoteNotFoundException(Long noteId) {
        super("Note not found with ID: " + noteId);
        this.noteId = noteId;
    }

    public NoteNotFoundException(String message) {
        super(message);
        this.noteId = null;
    }

    public Long getNoteId() {
        return noteId;
    }
}
