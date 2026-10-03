package com.student.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object (DTO) for incoming Note creation/update requests.
 * Uses Bean Validation annotations to enforce input constraints.
 * Keeps API input validation separate from the domain model.
 */
public class NoteRequestDTO {

    @NotBlank(message = "Title must not be blank")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Subject must not be blank")
    @Size(min = 2, max = 50, message = "Subject must be between 2 and 50 characters")
    private String subject;

    @NotBlank(message = "Content must not be blank")
    @Size(min = 5, max = 2000, message = "Content must be between 5 and 2000 characters")
    private String content;

    @NotBlank(message = "Student name must not be blank")
    @Size(min = 2, max = 80, message = "Student name must be between 2 and 80 characters")
    private String studentName;

    // ─── Constructors ──────────────────────────────────────────────────────────

    public NoteRequestDTO() {}

    public NoteRequestDTO(String title, String subject, String content, String studentName) {
        this.title       = title;
        this.subject     = subject;
        this.content     = content;
        this.studentName = studentName;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public String getTitle()                         { return title; }
    public void setTitle(String title)               { this.title = title; }

    public String getSubject()                       { return subject; }
    public void setSubject(String subject)           { this.subject = subject; }

    public String getContent()                       { return content; }
    public void setContent(String content)           { this.content = content; }

    public String getStudentName()                   { return studentName; }
    public void setStudentName(String studentName)   { this.studentName = studentName; }
}
