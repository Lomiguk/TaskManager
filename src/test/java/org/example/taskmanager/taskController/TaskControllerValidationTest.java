package org.example.taskmanager.taskController;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.taskmanager.api.enums.TaskPriority;
import org.example.taskmanager.api.enums.TaskStatus;
import org.example.taskmanager.api.request.task.AddTaskRequest;
import org.example.taskmanager.api.request.task.PutTaskRequest;
import org.example.taskmanager.controller.TaskController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class TaskControllerValidationTest {

    @MockBean
    private TaskController taskController;
    private MockMvc mockMvc;
    private Validator validator;

    // AddProfileRequest

    @BeforeEach
    void setUp() {
        taskController = mock(TaskController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    // AddTaskRequest

    @Test
    void testSaveTaskOK() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");

        var request = AddTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testSaveTaskBadRequest_WrongLabelSize() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");
        var request = AddTaskRequest.builder()
                .label("")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testSaveTaskBadRequest_NullLabel() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");
        var request = AddTaskRequest.builder()
                .label(null)
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testSaveTaskBadRequest_NullStatus() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");
        var request = AddTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(null)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testSaveTaskBadRequest_NullPriority() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");

        var request = AddTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(null)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testSaveTaskBadRequest_NullAuthorId() {
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");

        var request = AddTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(null)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    // PutTaskRequest

    @Test
    void testPutTaskOK() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");

        var request = PutTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testPutTaskBadRequest_WrongLabelSize() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");
        var request = PutTaskRequest.builder()
                .label("")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutTaskBadRequest_NullLabel() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");
        var request = PutTaskRequest.builder()
                .label(null)
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutTaskBadRequest_NullStatus() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");
        var request = PutTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(null)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutTaskBadRequest_NullPriority() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");

        var request = PutTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(null)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutTaskBadRequest_NullAuthorId() {
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");

        var request = PutTaskRequest.builder()
                .label("Test task")
                .description("Test description")
                .status(TaskStatus.WAITING)
                .priority(TaskPriority.LOW)
                .authorId(null)
                .executorId(executorId)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    // Get all with pagination task request

    @Test
    void testGetAllWithPaginationTask_ValidationSuccess() throws Exception {
        var pageNumber = 0;
        var pageSize = 10;

        mockMvc.perform(get("/tasks")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetAllWithPaginationTask_ValidationFailureByPageSize() throws Exception {
        var pageNumber = 0;
        var pageSize = -10;

        mockMvc.perform(get("/tasks")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testGetAllWithPaginationTask_ValidationFailureByPageNumber() throws Exception {
        var pageNumber = -10;
        var pageSize = 0;

        mockMvc.perform(get("/tasks")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
