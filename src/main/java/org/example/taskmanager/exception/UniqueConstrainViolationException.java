package org.example.taskmanager.exception;

public class UniqueConstrainViolationException extends RuntimeException {

    public UniqueConstrainViolationException(String message) {
        super(message);
    }
}
