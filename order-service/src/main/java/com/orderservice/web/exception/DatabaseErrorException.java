package com.orderservice.web.exception;

public class DatabaseErrorException extends RuntimeException {
    public DatabaseErrorException() {
        super();
    }

    public DatabaseErrorException(String message) {
        super(message);
    }

    public DatabaseErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseErrorException(Throwable cause) {
        super(cause);
    }
}
