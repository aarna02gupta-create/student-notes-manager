package com.student.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Student Notes Manager Application.
 * Uses Spring Boot auto-configuration.
 */
@SpringBootApplication
public class NotesManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesManagerApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  Student Notes Manager is running!");
        System.out.println("  Access: http://localhost:8080/api/notes");
        System.out.println("===========================================");
    }
}
