package org.example.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.task.AddTaskRequest;
import org.example.taskmanager.api.request.task.PutTaskRequest;
import org.example.taskmanager.api.response.TaskCommentResponse;
import org.example.taskmanager.api.response.TaskResponse;
import org.example.taskmanager.service.interfaces.TaskCommentService;
import org.example.taskmanager.service.interfaces.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskCommentService taskCommentServiceImpl;

    /**
     * Saving: Creating new Task in db
     *
     * @param request RequestBody with new Task data
     * @return ResponseEntity with new Task data as response body
     */
    @Operation(summary = "Creating new Task")
    @PostMapping
    public ResponseEntity<TaskResponse> save(
            @Valid
            @RequestBody
            AddTaskRequest request
    ) {
        return new ResponseEntity<>(
                taskService.create(request),
                HttpStatus.CREATED
        );
    }

    /**
     * Getting task's data by id
     *
     * @param id Unique task identifier
     * @return ResponseEntity with task data as response body
     */
    @Operation(summary = "Getting task's data by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(
            @PathVariable("id")
            UUID id
    ) {
        return new ResponseEntity<>(
                taskService.getById(id),
                HttpStatus.OK
        );
    }

    /**
     * Getting collection of tasks with pagination
     *
     * @param pageSize   quantity of tasks dto by request
     * @param pageNumber offset
     * @return ResponseEntity with collection of task's data as response body
     */
    @Operation(summary = "Getting collection of tasks with pagination")
    @GetMapping
    public ResponseEntity<Collection<TaskResponse>> getAllWithPagination(
            @PositiveOrZero
            @RequestParam("pageSize")
            Integer pageSize,
            @PositiveOrZero
            @RequestParam("pageNumber")
            Integer pageNumber
    ) {
        return new ResponseEntity<>(
                taskService.getAllWithPagination(pageSize, pageNumber),
                HttpStatus.OK
        );
    }

    /**
     * Getting task's comments by task id
     *
     * @param id         Unique task identifier
     * @param pageSize   quantity of comments dto by request
     * @param pageNumber offset
     * @return ResponseEntity with collection of task's comments data as response body
     */
    @Operation(summary = "Getting task's comments by task id")
    @GetMapping("/{id}/comments")
    public ResponseEntity<Collection<TaskCommentResponse>> getComments(
            @PathVariable
            UUID id,
            @PositiveOrZero
            @RequestParam("pageSize")
            Integer pageSize,
            @PositiveOrZero
            @RequestParam("pageNumber")
            Integer pageNumber
    ) {
        return new ResponseEntity<>(
                taskCommentServiceImpl.getAllByTaskWithPagination(id, pageNumber, pageSize),
                HttpStatus.OK
        );
    }

    /**
     * Put update of task's data
     *
     * @param id      Unique task identifier
     * @param request request body with new task's data
     * @return ResponseEntity with new saves task's data
     */
    @Operation(summary = "Put update of task's data")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> putUpdate(
            @PathVariable("id")
            UUID id,
            @RequestBody
            PutTaskRequest request
    ) {
        return new ResponseEntity<>(
                taskService.putUpdate(id, request),
                HttpStatus.OK
        );
    }

    /**
     * Patch update of the task's status
     *
     * @param id     Unique task identifier
     * @param status Request param with new task status
     * @return ResponseEntity with task's data after the update
     */
    @Operation(summary = "Patch update of the task's status")
    @PatchMapping("/{id}/statuses")
    public ResponseEntity<TaskResponse> patchStatus(
            @PathVariable
            UUID id,
            @RequestParam("status")
            String status
    ) {
        return new ResponseEntity<>(
                taskService.patchStatus(id, status),
                HttpStatus.OK
        );
    }

    /**
     * Deleting Task data from db
     *
     * @param id Unique task identifier
     * @return ResponseEntity with boolean flag - success/failure as response body
     */
    @Operation(summary = "Deleting Task data from db")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(
                taskService.delete(id),
                HttpStatus.OK
        );
    }
}
