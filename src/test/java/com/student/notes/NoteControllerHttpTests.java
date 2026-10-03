package com.student.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.notes.controller.NoteController;
import com.student.notes.dto.NoteRequestDTO;
import com.student.notes.exception.GlobalExceptionHandler;
import com.student.notes.model.Note;
import com.student.notes.repository.NoteRepository;
import com.student.notes.service.NoteServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Exercises real MVC routing, JSON conversion, Bean Validation, and exception advice. */
class NoteControllerHttpTests {
    private final ObjectMapper json = new ObjectMapper();
    private LocalValidatorFactoryBean validator;
    private NoteServiceImpl service;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        service = new NoteServiceImpl(new NoteRepository());
        mvc = controllerMvc(service);
    }

    private MockMvc controllerMvc(NoteServiceImpl noteService) {
        return MockMvcBuilders.standaloneSetup(new NoteController(noteService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @AfterEach
    void closeValidator() {
        validator.close();
    }

    private Map<String, String> validNote() {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("title", "Normalization");
        body.put("subject", "DBMS");
        body.put("content", "Revision notes covering 3NF and BCNF.");
        body.put("studentName", "Aarna");
        return body;
    }

    @Test
    void createReadUpdateAndDeleteThroughHttp() throws Exception {
        String response = mvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(validNote())))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Normalization"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andReturn().getResponse().getContentAsString();
        long id = json.readTree(response).path("data").path("id").asLong();

        mvc.perform(get("/api/notes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id));

        Map<String, String> updated = validNote();
        updated.put("title", "Updated normalization");
        mvc.perform(put("/api/notes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id))
                .andExpect(jsonPath("$.data.title").value("Updated normalization"));

        mvc.perform(delete("/api/notes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
        mvc.perform(get("/api/notes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
        assertEquals(0, service.getTotalCount());
    }

    static Stream<Arguments> invalidFields() {
        return Stream.of(
                Arguments.of("title", " "),
                Arguments.of("subject", " "),
                Arguments.of("content", " "),
                Arguments.of("studentName", " "),
                Arguments.of("title", "a"),
                Arguments.of("title", "a".repeat(101)),
                Arguments.of("subject", "a"),
                Arguments.of("subject", "a".repeat(51)),
                Arguments.of("content", "abcd"),
                Arguments.of("content", "a".repeat(2001)),
                Arguments.of("studentName", "a"),
                Arguments.of("studentName", "a".repeat(81)));
    }

    @ParameterizedTest(name = "Reject invalid {0}")
    @MethodSource("invalidFields")
    void invalidCreateReturnsFieldErrorWithoutSaving(String field, String value) throws Exception {
        Map<String, String> body = validNote();
        body.put(field, value);
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed. Please check your input."))
                .andExpect(jsonPath("$.data." + field).isString())
                .andExpect(jsonPath("$.timestamp").exists());
        assertEquals(0, service.getTotalCount());
    }

    @Test
    void missingRequiredFieldReturns400() throws Exception {
        Map<String, String> body = validNote();
        body.remove("studentName");
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.studentName").isString());
        assertEquals(0, service.getTotalCount());
    }

    static Stream<Arguments> validBoundaries() {
        return Stream.of(Arguments.of(2, 2, 5, 2), Arguments.of(100, 50, 2000, 80));
    }

    @ParameterizedTest
    @MethodSource("validBoundaries")
    void acceptsMinimumAndMaximumFieldLengths(int title, int subject, int content, int name) throws Exception {
        Map<String, String> body = validNote();
        body.put("title", "a".repeat(title));
        body.put("subject", "a".repeat(subject));
        body.put("content", "a".repeat(content));
        body.put("studentName", "a".repeat(name));
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
        assertEquals(1, service.getTotalCount());
    }

    @Test
    void invalidUpdateLeavesStoredNoteUntouched() throws Exception {
        Note existing = service.createNote(new NoteRequestDTO("Original", "DBMS", "Valid content", "Aarna"));
        Map<String, String> body = validNote();
        body.put("title", "");
        mvc.perform(put("/api/notes/{id}", existing.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.title").isString());
        assertEquals("Original", service.getNoteById(existing.getId()).getTitle());
    }

    static Stream<HttpMethod> missingNoteMethods() {
        return Stream.of(HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE);
    }

    @ParameterizedTest
    @MethodSource("missingNoteMethods")
    void missingNoteReturns404ForReadUpdateAndDelete(HttpMethod method) throws Exception {
        mvc.perform(request(method, "/api/notes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(validNote())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void malformedJsonReturns400WithoutSaving() throws Exception {
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
        assertEquals(0, service.getTotalCount());
    }

    @Test
    void absentRequestBodyReturns400() throws Exception {
        mvc.perform(post("/api/notes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
        assertEquals(0, service.getTotalCount());
    }

    @Test
    void unsupportedMethodKeeps405AndAllowHeader() throws Exception {
        mvc.perform(patch("/api/notes/1").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(validNote())))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().string("Allow", containsString("PUT")))
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void unsupportedContentTypeKeeps415() throws Exception {
        mvc.perform(post("/api/notes").contentType(MediaType.TEXT_PLAIN).content("not JSON"))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.success").value(false));
        assertEquals(0, service.getTotalCount());
    }

    @Test
    void nonNumericIdReturns400() throws Exception {
        mvc.perform(get("/api/notes/not-a-number"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void missingFilterParameterReturns400() throws Exception {
        mvc.perform(get("/api/notes/student"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void unexpectedFailureReturns500WithoutInternalDetails() throws Exception {
        NoteServiceImpl failingService = new NoteServiceImpl(new NoteRepository()) {
            @Override
            public Note getNoteById(Long id) {
                throw new IllegalStateException("internal connection details");
            }
        };
        controllerMvc(failingService).perform(get("/api/notes/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."))
                .andExpect(content().string(not(containsString("internal connection details"))));
    }
}
