package com.layby.web.exception;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException() {
        super();
    }
    public ValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidationFailedException(String message) {
        super(message);
    }
    public ValidationFailedException(Throwable cause) {
        super(cause);
    }
}