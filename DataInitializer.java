package com.student.notes.config;

import com.student.notes.model.Note;
import com.student.notes.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data Initializer that pre-loads sample notes into the in-memory store
 * when the application starts.
 *
 * Advanced Java Concepts:
 * - Implements CommandLineRunner: Spring calls run() after the application context loads.
 * - @Component: Registers this class as a Spring Bean.
 * - @Autowired: Injects NoteRepository via constructor DI.
 *
 * This simulates having "seed data" without needing a database.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;

    @Autowired
    public DataInitializer(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Runs once on application startup.
     * Seeds 5 sample notes into the in-memory ConcurrentHashMap store.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("---------------------------------------------");
        System.out.println("  Loading sample data into memory...");
        System.out.println("---------------------------------------------");

        noteRepository.save(new Note(null,
                "Introduction to Java",
                "Computer Science",
                "Java is a high-level, class-based, object-oriented programming language. " +
                "Key features include platform independence (Write Once Run Anywhere), " +
                "automatic memory management (Garbage Collection), and strong type safety.",
                "Alice Johnson"));

        noteRepository.save(new Note(null,
                "Newton's Laws of Motion",
                "Physics",
                "1st Law: An object at rest stays at rest unless acted upon by a net force. " +
                "2nd Law: Force = Mass × Acceleration (F = ma). " +
                "3rd Law: For every action, there is an equal and opposite reaction.",
                "Bob Smith"));

        noteRepository.save(new Note(null,
                "Differentiation Basics",
                "Mathematics",
                "Differentiation finds the rate of change of a function. " +
                "Key rules: Power Rule — d/dx(x^n) = n*x^(n-1). " +
                "Chain Rule — d/dx[f(g(x))] = f'(g(x)) * g'(x).",
                "Alice Johnson"));

        noteRepository.save(new Note(null,
                "Photosynthesis",
                "Biology",
                "Photosynthesis converts light energy into chemical energy stored in glucose. " +
                "Equation: 6CO2 + 6H2O + light → C6H12O6 + 6O2. " +
                "Takes place in chloroplasts inside plant cells.",
                "Carol White"));

        noteRepository.save(new Note(null,
                "Spring Boot Overview",
                "Computer Science",
                "Spring Boot simplifies Spring application development. " +
                "It uses auto-configuration, embedded servers (Tomcat), and starter dependencies. " +
                "Key annotations: @SpringBootApplication, @RestController, @Service, @Repository.",
                "Bob Smith"));

        System.out.println("  Sample data loaded! Total notes: " + noteRepository.count());
        System.out.println("---------------------------------------------");
    }
}
