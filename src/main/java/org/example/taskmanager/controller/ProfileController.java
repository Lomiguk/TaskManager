package org.example.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.ProfileRequest;
import org.example.taskmanager.api.request.PutProfileRequest;
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
    public ResponseEntity<ProfileResponse> save(@RequestBody ProfileRequest profile) {
        return new ResponseEntity<>(
                profileService.create(profile),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Collection<ProfileResponse>> getAllWithPagination(
            @RequestParam("limit")
            Integer limit,
            @RequestParam("offset")
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
            @RequestBody PutProfileRequest profile
    ) {
        return new ResponseEntity<>(
                profileService.update(id, profile),
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
