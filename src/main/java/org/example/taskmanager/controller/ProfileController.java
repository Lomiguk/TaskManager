package org.example.taskmanager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.api.response.TaskResponse;
import org.example.taskmanager.service.interfaces.ProfileService;
import org.example.taskmanager.service.interfaces.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ProfileResponse> save(
            @Valid
            @RequestBody
            AddProfileRequest request
    ) {
        return new ResponseEntity<>(
                profileService.create(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Collection<ProfileResponse>> getAllWithPagination(
            @RequestParam("pageSize")
            @PositiveOrZero(message = "pageSize must be positive or zero")
            Integer pageSize,
            @RequestParam("pageNumber")
            @PositiveOrZero(message = "pageNumber must be positive or zero")
            Integer pageNumber
    ) {
        return new ResponseEntity<>(
                profileService.getAllWithPagination(pageSize, pageNumber),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> get(@PathVariable UUID id) {
        return new ResponseEntity<>(
                profileService.getById(id),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            PutProfileRequest request
    ) {
        return new ResponseEntity<>(
                profileService.update(id, request),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<Collection<TaskResponse>> getTasks(
            @PathVariable
            UUID id,
            @RequestParam("status")
            String status
    ) {
        return new ResponseEntity<>(
                taskService.getByProfile(id, status),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(
                profileService.delete(id),
                HttpStatus.OK
        );
    }
}
