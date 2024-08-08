package org.example.taskmanager;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.example.taskmanager.controller.ProfileController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ProfileValidationTest {

    @MockBean
    private ProfileController profileController;
    private MockMvc mockMvc;
    private Validator validator;

    // AddProfileRequest

    @BeforeEach
    void setUp() {
        profileController = mock(ProfileController.class);
        mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void testSaveProfileBadRequest_WrongNameSize() {
        var request = AddProfileRequest.builder()
                .name("")
                .email("t.name@mail.com")
                .password("TestPassword")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testSaveProfileBadRequest_NullName() {
        var request = AddProfileRequest.builder()
                .email("t.name@mail.com")
                .password("TestPassword")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testSaveProfileBadRequest_WrongEmailFormat() {
        var request = AddProfileRequest.builder()
                .name("Test Name")
                .email("name.com")
                .password("TestPassword")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testSaveProfileBadRequest_NullEmail() {
        var request = AddProfileRequest.builder()
                .name("Test Name")
                .password("TestPassword")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testSaveProfileBadRequest_WrongPasswordSize() {
        var request = AddProfileRequest.builder()
                .name("Test Name")
                .email("t.name@mail.com")
                .password("Test")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    // PutProfileRequest

    @Test
    void testPutProfile_WrongNameSize() {
        var request = PutProfileRequest.builder()
                .name("")
                .email("t.name@mail.com")
                .password("TestPassword")
                .isActive(true)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testPutProfile_NullName() {
        var request = PutProfileRequest.builder()
                .email("t.name@mail.com")
                .password("TestPassword")
                .isActive(true)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutProfile_wrongEmailFormat() {
        var request = PutProfileRequest.builder()
                .name("Test Name")
                .email("name.com")
                .password("TestPassword")
                .isActive(true)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutProfile_NullEmail() {
        var request = PutProfileRequest.builder()
                .name("Test Name")
                .password("TestPassword")
                .isActive(true)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutProfile_WrongPasswordSize() {
        var request = PutProfileRequest.builder()
                .name("Test Name")
                .email("t.name@mail.com")
                .password("Test")
                .isActive(true)
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    @Test
    void testPutProfile_NullIsActiveField() {
        var request = PutProfileRequest.builder()
                .name("Test Name")
                .email("t.name@mail.com")
                .password("Test")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }

    // Get all with pagination profile request

    @Test
    void testGetAllWithPaginationProfile_ValidationSuccess() throws Exception {
        var pageNumber = 0;
        var pageSize = 10;

        mockMvc.perform(get("/profile")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetAllWithPaginationProfile_ValidationFailureByPageSize() throws Exception {
        var pageNumber = 0;
        var pageSize = -10;

        mockMvc.perform(get("/profile")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testGetAllWithPaginationProfile_ValidationFailureByPageNumber() throws Exception {
        var pageNumber = -10;
        var pageSize = 0;

        mockMvc.perform(get("/profile")
                .param("pageNumber", String.valueOf(pageNumber))
                .param("pageSize", String.valueOf(pageSize))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
