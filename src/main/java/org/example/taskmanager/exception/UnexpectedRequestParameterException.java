package org.example.taskmanager.exception;

public class UnexpectedRequestParameterException extends RuntimeException{

    public UnexpectedRequestParameterException(String message) {
        super(message);
    }
}
