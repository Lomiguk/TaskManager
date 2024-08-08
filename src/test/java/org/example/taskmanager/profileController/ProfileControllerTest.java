package org.example.taskmanager.profileController;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.response.ProfileResponse;
import org.example.taskmanager.controller.ProfileController;
import org.example.taskmanager.service.interfaces.ProfileService;
import org.example.taskmanager.service.interfaces.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProfileControllerTest {

    private ProfileService profileService;
    @MockBean
    private ProfileController profileController;
    private Validator validator;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        var taskService = mock(TaskService.class);
        profileController = new ProfileController(profileService, taskService);

        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testSaveProfileSuccess() {
        var request = AddProfileRequest.builder()
                .name("Test Name")
                .email("t.name@mail.com")
                .password("TestPassword")
                .build();
        var mockedResponse = new ProfileResponse(UUID.randomUUID(), "Test Name", "t.name@mail.com", true);

        var violations = validator.validate(request);

        assertTrue(violations.isEmpty());

        when(profileService.create(request)).thenReturn(mockedResponse);
        var responseEntity = profileController.save(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(mockedResponse, responseEntity.getBody());
    }
}
