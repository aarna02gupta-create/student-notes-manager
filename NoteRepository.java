package com.student.notes.repository;

import com.student.notes.model.Note;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-Memory Repository for storing Student Notes.
 *
 * Advanced Java Concepts used:
 * - ConcurrentHashMap: Thread-safe in-memory data store (no database needed)
 * - AtomicLong: Thread-safe auto-incrementing ID generator
 * - Optional<T>: Null-safe return type for single note lookups
 * - Java Streams + Lambda: Filtering and searching through notes
 * - Generics: Used via List<Note>, Optional<Note>
 *
 * Annotated with @Repository so Spring manages it as a Bean.
 */
@Repository
public class NoteRepository {

    /**
     * ConcurrentHashMap as the in-memory "database".
     * Key   = Note ID (Long)
     * Value = Note object
     * Thread-safe for concurrent access.
     */
    private final Map<Long, Note> noteStore = new ConcurrentHashMap<>();

    /**
     * AtomicLong for thread-safe auto-increment ID generation.
     * Starts at 1 and increments by 1 for each new note.
     */
    private final AtomicLong idGenerator = new AtomicLong(1);

    // ─── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Saves a new Note into the in-memory store.
     * Assigns a unique auto-incremented ID before saving.
     *
     * @param note the Note object to persist
     * @return the saved Note with its generated ID
     */
    public Note save(Note note) {
        long newId = idGenerator.getAndIncrement();
        note.setId(newId);
        noteStore.put(newId, note);
        return note;
    }

    // ─── READ (All) ───────────────────────────────────────────────────────────

    /**
     * Retrieves all notes from the in-memory store.
     * Uses Java Streams to collect map values into a sorted List.
     *
     * @return List of all Note objects sorted by ID
     */
    public List<Note> findAll() {
        return noteStore.values()
                        .stream()
                        .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                        .collect(Collectors.toList());
    }

    // ─── READ (By ID) ─────────────────────────────────────────────────────────

    /**
     * Finds a Note by its unique ID.
     * Returns Optional<Note> — avoids NullPointerException at the caller.
     *
     * @param id the Note ID to search for
     * @return Optional containing the Note if found, or empty Optional
     */
    public Optional<Note> findById(Long id) {
        return Optional.ofNullable(noteStore.get(id));
    }

    // ─── READ (By Student) ────────────────────────────────────────────────────

    /**
     * Finds all Notes belonging to a specific student.
     * Uses Java Streams + Lambda expressions to filter by studentName.
     *
     * @param studentName the name of the student to filter by
     * @return List of Notes for the given student
     */
    public List<Note> findByStudentName(String studentName) {
        return noteStore.values()
                        .stream()
                        .filter(note -> note.getStudentName()
                                            .equalsIgnoreCase(studentName))
                        .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                        .collect(Collectors.toList());
    }

    // ─── READ (By Subject) ────────────────────────────────────────────────────

    /**
     * Finds all Notes for a specific subject.
     * Uses Java Streams + Lambda expressions to filter by subject.
     *
     * @param subject the subject name to filter by
     * @return List of Notes for the given subject
     */
    public List<Note> findBySubject(String subject) {
        return noteStore.values()
                        .stream()
                        .filter(note -> note.getSubject()
                                            .equalsIgnoreCase(subject))
                        .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                        .collect(Collectors.toList());
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Updates an existing Note in the in-memory store.
     * Checks existence first; replaces the entire Note object if found.
     *
     * @param id          the ID of the Note to update
     * @param updatedNote the new Note data to replace with
     * @return Optional containing the updated Note, or empty if not found
     */
    public Optional<Note> update(Long id, Note updatedNote) {
        if (!noteStore.containsKey(id)) {
            return Optional.empty();
        }
        updatedNote.setId(id);
        noteStore.put(id, updatedNote);
        return Optional.of(updatedNote);
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Deletes a Note from the in-memory store by its ID.
     *
     * @param id the ID of the Note to delete
     * @return true if the Note existed and was deleted, false otherwise
     */
    public boolean deleteById(Long id) {
        return noteStore.remove(id) != null;
    }

    // ─── UTILITY ─────────────────────────────────────────────────────────────

    /**
     * Returns the total number of notes in the store.
     *
     * @return count of stored notes
     */
    public long count() {
        return noteStore.size();
    }

    /**
     * Returns true if a note with the given ID exists.
     *
     * @param id the Note ID to check
     * @return true if found, false otherwise
     */
    public boolean existsById(Long id) {
        return noteStore.containsKey(id);
    }
}
