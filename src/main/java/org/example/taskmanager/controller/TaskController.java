package org.example.taskmanager.controller;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(
                taskService.delete(id),
                HttpStatus.OK
        );
    }
}
