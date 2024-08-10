package org.example.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.taskComment.AddTaskCommentRequest;
import org.example.taskmanager.api.request.taskComment.PutTaskCommentRequest;
import org.example.taskmanager.api.response.TaskCommentResponse;
import org.example.taskmanager.service.interfaces.TaskCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks/comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    /**
     * Saving: Create new Task comment in db
     *
     * @param request Request body with new task's comment data
     * @return ResponseEntity with new task's comment data as response body
     */
    @Operation(summary = "Create new Task comment")
    @PostMapping
    public ResponseEntity<TaskCommentResponse> create(
            @Valid
            @RequestBody
            AddTaskCommentRequest request
    ) {
        return new ResponseEntity<>(
                taskCommentService.create(request),
                HttpStatus.CREATED
        );
    }

    /**
     * Getting Task's comment by id
     *
     * @param id Unique comment identifier
     * @return ResponseEntity with task's comment data as response body
     */
    @Operation(summary = "Getting Task's comment by id")
    @GetMapping("/{id}")
    public ResponseEntity<TaskCommentResponse> getById(@PathVariable UUID id) {
        return new ResponseEntity<>(
                taskCommentService.getById(id),
                HttpStatus.OK
        );
    }

    /**
     * Put update of task's comment by id
     *
     * @param id Unique comment identifier
     * @param request Request body with new comment data
     * @return ResponseEntity with new  saved comment data
     */
    @Operation(summary = "Put update of task's comment by id")
    @PutMapping("/{id}")
    public ResponseEntity<TaskCommentResponse> update(
            @PathVariable
            UUID id,
            @Valid
            @RequestBody
            PutTaskCommentRequest request
    ) {
        return new ResponseEntity<>(
                taskCommentService.update(id, request),
                HttpStatus.OK
        );
    }

    /**
     * Deleting task's comment by id
     *
     * @param id Unique comment identifier
     * @return ResponseEntity with boolean flag - success/failure as response body
     */
    @Operation(summary = "Deleting task's comment by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(
                taskCommentService.delete(id),
                HttpStatus.OK
        );
    }
}
