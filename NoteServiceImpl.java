package com.student.notes.service;

import com.student.notes.dto.NoteRequestDTO;
import com.student.notes.exception.NoteNotFoundException;
import com.student.notes.model.Note;
import com.student.notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service Implementation for Student Notes Manager.
 *
 * Advanced Java Concepts used:
 * - @Service: Spring stereotype annotation marking this as a Service Bean
 * - @Autowired: Constructor Injection for Dependency Injection (IoC)
 * - Interface Implementation: Implements NoteService for loose coupling
 * - Custom Exception Propagation: Throws NoteNotFoundException on missing IDs
 * - Object Mapping: Manually maps DTO → Model (without external libraries)
 *
 * @see NoteService
 * @see NoteRepository
 */
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    /**
     * Constructor-based Dependency Injection.
     * Spring automatically injects NoteRepository when creating this bean.
     *
     * @param noteRepository the in-memory repository
     */
    @Autowired
    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────

    /**
     * Creates a new note by mapping DTO fields to the Note model,
     * then delegates to the repository for storage.
     *
     * @param dto incoming request data
     * @return the saved Note with generated ID
     */
    @Override
    public Note createNote(NoteRequestDTO dto) {
        Note note = mapDtoToNote(dto);
        Note saved = noteRepository.save(note);
        System.out.println("[CREATE] New note saved → " + saved);
        return saved;
    }

    // ─── READ (All) ───────────────────────────────────────────────────────────

    /**
     * Delegates to repository to fetch all notes.
     *
     * @return List of all stored notes
     */
    @Override
    public List<Note> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        System.out.println("[READ] Fetched all notes. Total: " + notes.size());
        return notes;
    }

    // ─── READ (By ID) ─────────────────────────────────────────────────────────

    /**
     * Fetches a note by ID. Uses Optional.orElseThrow() to handle the
     * missing-note case elegantly without explicit null checks.
     *
     * Advanced Java: Optional chaining with method reference
     *
     * @param id the note ID
     * @return the found Note
     * @throws NoteNotFoundException if no note with that ID exists
     */
    @Override
    public Note getNoteById(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
    }

    // ─── READ (By Student) ────────────────────────────────────────────────────

    /**
     * Fetches all notes for a given student name.
     *
     * @param studentName the student's name
     * @return filtered List of Notes
     */
    @Override
    public List<Note> getNotesByStudent(String studentName) {
        List<Note> notes = noteRepository.findByStudentName(studentName);
        System.out.println("[READ] Notes for student '" + studentName + "': " + notes.size());
        return notes;
    }

    // ─── READ (By Subject) ────────────────────────────────────────────────────

    /**
     * Fetches all notes for a given subject.
     *
     * @param subject the subject name
     * @return filtered List of Notes
     */
    @Override
    public List<Note> getNotesBySubject(String subject) {
        List<Note> notes = noteRepository.findBySubject(subject);
        System.out.println("[READ] Notes for subject '" + subject + "': " + notes.size());
        return notes;
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    /**
     * Updates an existing note by ID with new values from the DTO.
     * The original createdAt timestamp is preserved; updatedAt is refreshed.
     *
     * @param id  the note ID to update
     * @param dto the new data
     * @return the updated Note
     * @throws NoteNotFoundException if no note with that ID exists
     */
    @Override
    public Note updateNote(Long id, NoteRequestDTO dto) {
        // Verify the note exists first
        Note existing = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));

        // Map new values from DTO
        Note updatedNote = mapDtoToNote(dto);

        // Preserve original creation time, refresh update time
        updatedNote.setCreatedAt(existing.getCreatedAt());
        updatedNote.setUpdatedAt(LocalDateTime.now());

        Note result = noteRepository.update(id, updatedNote)
                .orElseThrow(() -> new NoteNotFoundException(id));

        System.out.println("[UPDATE] Note updated → " + result);
        return result;
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    /**
     * Deletes a note by ID.
     * Throws NoteNotFoundException if the note doesn't exist.
     *
     * @param id the note ID to delete
     * @throws NoteNotFoundException if no note with that ID exists
     */
    @Override
    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
        System.out.println("[DELETE] Note with ID " + id + " deleted.");
    }

    // ─── UTILITY ─────────────────────────────────────────────────────────────

    /**
     * Returns total count of notes in the system.
     *
     * @return total note count
     */
    @Override
    public long getTotalCount() {
        return noteRepository.count();
    }

    // ─── Private Helper: DTO → Model Mapping ─────────────────────────────────

    /**
     * Maps fields from NoteRequestDTO to a new Note domain object.
     * This keeps the mapping logic in one place (DRY principle).
     *
     * @param dto the incoming request DTO
     * @return a new Note object populated with DTO values
     */
    private Note mapDtoToNote(NoteRequestDTO dto) {
        Note note = new Note();
        note.setTitle(dto.getTitle().trim());
        note.setSubject(dto.getSubject().trim());
        note.setContent(dto.getContent().trim());
        note.setStudentName(dto.getStudentName().trim());
        return note;
    }
}
