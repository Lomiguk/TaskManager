package org.example.taskmanager.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.service.implement.ProfileServiceImpl;
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
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileService;

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
            @RequestParam("limit")
            @PositiveOrZero(message = "Limit must be positive or zero")
            Integer limit,
            @RequestParam("offset")
            @PositiveOrZero(message = "Offset must be positive or zero")
            Integer offset
    ) {
        return new ResponseEntity<>(
                profileService.getAllWithPagination(limit, offset),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> get(@PathVariable UUID id) {
        return new ResponseEntity<>(
                profileService.get(id),
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(
                profileService.delete(id),
                HttpStatus.OK
        );
    }
}
