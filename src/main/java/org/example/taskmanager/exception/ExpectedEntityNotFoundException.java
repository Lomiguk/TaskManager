package org.example.taskmanager.exception;

public class ExpectedEntityNotFoundException extends RuntimeException {
    public ExpectedEntityNotFoundException(String message) {
        super(message);
    }
}
