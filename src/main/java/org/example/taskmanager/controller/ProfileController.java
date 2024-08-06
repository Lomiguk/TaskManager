package org.example.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskmanager.api.request.AddProfileRequest;
import org.example.taskmanager.api.request.PutProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.exception.ExpectedEntityNotFoundException;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileService;

    @PostMapping
    public ResponseEntity<ProfileResponse> save(@RequestBody AddProfileRequest profile) {
        return new ResponseEntity<>(
                profileService.create(profile),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Collection<ProfileResponse>> getAll() {
        //TODO(Not implemented yet)
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> get(@PathVariable UUID id) {
        try {
            return new ResponseEntity<>(
                    profileService.get(id),
                    HttpStatus.OK
            );
        } catch (ExpectedEntityNotFoundException e) {
            return new ResponseEntity<>(
                    null,
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> update(
            @PathVariable long id,
            @RequestBody PutProfileRequest profile
    ) {
        // TODO(Not implemented yet)
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return new ResponseEntity<>(false, HttpStatus.LOCKED);
    }
}
