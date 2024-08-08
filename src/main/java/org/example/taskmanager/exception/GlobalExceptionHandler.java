package org.example.taskmanager.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex
    ) {
        LOGGER.info("Validation Exceptions");
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            LOGGER.info(" - Validation Exception: " + errorMessage);
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> constraintViolationException(
            ConstraintViolationException ex
    ) {
        var errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        var result = new HashMap<String, List<String>>();

        result.put("errors", errors);

        LOGGER.info("Constraint violation exception: " + errors);

        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler(ExpectedEntityNotFoundException.class)
    protected ResponseEntity<String> handleExpectedEntityNotFound(
            ExpectedEntityNotFoundException ex
    ) {
        LOGGER.info("Expected entity not found: " + ex.getMessage());

        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(UnexpectedRequestParameterException.class)
    protected ResponseEntity<String> handleUnexpectedRequestParameter(
            UnexpectedRequestParameterException ex
    ) {
        LOGGER.info("Unexpected request parameter: " + ex.getMessage());

        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
