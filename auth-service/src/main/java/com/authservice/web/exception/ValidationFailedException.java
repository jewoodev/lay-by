package com.authservice.web.exception;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException() {
        super();
    }

    public ValidationFailedException(String message) {
        super(message);
    }

    public ValidationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationFailedException(Throwable cause) {
        super(cause);
    }
}
