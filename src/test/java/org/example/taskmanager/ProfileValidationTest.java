package org.example.taskmanager;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.example.taskmanager.api.request.profile.AddProfileRequest;
import org.example.taskmanager.api.request.profile.PutProfileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProfileValidationTest {

    private Validator validator;

    // AddProfileRequest

    @BeforeEach
    void setUp() {
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
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testPutProfile_wrongEmailFormat() {
        var request = PutProfileRequest.builder()
                .name("Test Name")
                .email("name.com")
                .password("TestPassword")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testPutProfile_NullEmail() {
        var request = PutProfileRequest.builder()
                .name("Test Name")
                .password("TestPassword")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }

    @Test
    void testPutProfile_WrongPasswordSize() {
        var request = PutProfileRequest.builder()
                .name("Test Name")
                .email("t.name@mail.com")
                .password("Test")
                .build();

        var violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertEquals(violations.size(), 1);
    }
}
