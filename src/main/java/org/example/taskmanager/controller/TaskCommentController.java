package org.example.taskmanager.controller;

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

    @GetMapping("/{id}")
    public ResponseEntity<TaskCommentResponse> getById(@PathVariable UUID id) {
        return new ResponseEntity<>(
                taskCommentService.getById(id),
                HttpStatus.OK
        );
    }

    // There is no get all with pagination, because comments are part of tasks,
    // so they are received from TaskController

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(
                taskCommentService.delete(id),
                HttpStatus.OK
        );
    }
}
