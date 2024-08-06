package org.example.taskmanager.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

    protected ResponseEntity<String> handleExpectedEntityNotFound(
            ExpectedEntityNotFoundException ex
    ) {
        LOGGER.info("Expected entity not found");

        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
