package org.example.taskmanager.taskController;


import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.taskmanager.api.enums.TaskPriority;
import org.example.taskmanager.api.enums.TaskStatus;
import org.example.taskmanager.api.request.task.AddTaskRequest;
import org.example.taskmanager.api.request.task.PutTaskRequest;
import org.example.taskmanager.api.response.TaskResponse;
import org.example.taskmanager.controller.TaskController;
import org.example.taskmanager.service.interfaces.TaskCommentService;
import org.example.taskmanager.service.interfaces.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskControllerTest {

    private TaskService taskService;
    private TaskController taskController;
    private Validator validator;

    @BeforeEach
    void setUp() {
        taskService = mock(TaskService.class);
        TaskCommentService taskCommentService = mock(TaskCommentService.class);
        taskController = new TaskController(taskService, taskCommentService);

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testSaveTaskSuccess() {
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
        var responseId = UUID.randomUUID();
        var mockedResponse = new TaskResponse(
                responseId,
                "Test task",
                "Test description",
                TaskStatus.WAITING,
                TaskPriority.LOW,
                authorId,
                executorId
        );

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());

        when(taskService.create(request)).thenReturn(mockedResponse);
        var responseEntity = taskController.save(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockedResponse, responseEntity.getBody());
    }

    @Test
    void testPutTaskSuccess() {
        var authorId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        var executorId = UUID.fromString("3af85f64-5717-b3fc-4562-57173f66afa6");
        var taskId = UUID.randomUUID();

        var request = PutTaskRequest.builder()
                .label("new Test task label")
                .description("Test description")
                .status(TaskStatus.COMPLETED)
                .priority(TaskPriority.LOW)
                .authorId(authorId)
                .executorId(executorId)
                .build();

        var mockedResponse = new TaskResponse(
                taskId,
                "Test task",
                "Test description",
                TaskStatus.WAITING,
                TaskPriority.LOW,
                authorId,
                executorId
        );

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());

        when(taskService.putUpdate(taskId, request)).thenReturn(mockedResponse);
        var responseEntity = taskController.putUpdate(taskId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockedResponse, responseEntity.getBody());
    }
}
