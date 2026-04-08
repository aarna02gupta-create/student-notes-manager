package com.student.notes.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Model class representing a Student Note.
 * This is a Plain Java Object (POJO) — no database annotations needed.
 * Stores all note-related data in memory.
 */
public class Note {

    private Long id;
    private String title;
    private String subject;
    private String content;
    private String studentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ─── Constructors ──────────────────────────────────────────────────────────

    public Note() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Note(Long id, String title, String subject, String content, String studentName) {
        this.id          = id;
        this.title       = title;
        this.subject     = subject;
        this.content     = content;
        this.studentName = studentName;
        this.createdAt   = LocalDateTime.now();
        this.updatedAt   = LocalDateTime.now();
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getTitle()               { return title; }
    public void setTitle(String title)     { this.title = title; }

    public String getSubject()             { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent()             { return content; }
    public void setContent(String content) { this.content = content; }

    public String getStudentName()                   { return studentName; }
    public void setStudentName(String studentName)   { this.studentName = studentName; }

    public LocalDateTime getCreatedAt()              { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt){ this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt()              { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }

    // ─── toString ─────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Note{" +
               "id="          + id +
               ", title='"    + title    + '\'' +
               ", subject='"  + subject  + '\'' +
               ", student='"  + studentName + '\'' +
               ", createdAt=" + (createdAt != null ? createdAt.format(fmt) : "N/A") +
               ", updatedAt=" + (updatedAt != null ? updatedAt.format(fmt) : "N/A") +
               '}';
    }
}
