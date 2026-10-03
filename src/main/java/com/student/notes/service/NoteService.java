package com.student.notes.service;

import com.student.notes.dto.NoteRequestDTO;
import com.student.notes.model.Note;

import java.util.List;

/**
 * Service Interface for Student Notes Manager.
 *
 * Advanced Java Concept: Interface-based programming / Abstraction.
 * - Defines the contract for all CRUD operations.
 * - The actual implementation is in NoteServiceImpl.java.
 * - Spring injects the implementation at runtime (Dependency Injection).
 * - This enables loose coupling between Controller and Service layers.
 */
public interface NoteService {

    /**
     * Creates a new Note from the given request DTO.
     *
     * @param dto the incoming note data
     * @return the created Note with its assigned ID
     */
    Note createNote(NoteRequestDTO dto);

    /**
     * Retrieves all notes stored in the system.
     *
     * @return List of all Note objects
     */
    List<Note> getAllNotes();

    /**
     * Retrieves a single Note by its unique ID.
     * Throws NoteNotFoundException if no such note exists.
     *
     * @param id the unique identifier of the note
     * @return the found Note object
     */
    Note getNoteById(Long id);

    /**
     * Retrieves all notes belonging to a specific student.
     *
     * @param studentName the name of the student
     * @return List of Notes for that student
     */
    List<Note> getNotesByStudent(String studentName);

    /**
     * Retrieves all notes for a specific subject.
     *
     * @param subject the subject to filter by
     * @return List of Notes for that subject
     */
    List<Note> getNotesBySubject(String subject);

    /**
     * Updates an existing Note identified by ID with new data.
     * Throws NoteNotFoundException if no note with that ID exists.
     *
     * @param id  the ID of the note to update
     * @param dto the new note data
     * @return the updated Note object
     */
    Note updateNote(Long id, NoteRequestDTO dto);

    /**
     * Deletes a Note by its ID.
     * Throws NoteNotFoundException if no note with that ID exists.
     *
     * @param id the ID of the note to delete
     */
    void deleteNote(Long id);

    /**
     * Returns the total count of notes in the system.
     *
     * @return total number of notes
     */
    long getTotalCount();
}
