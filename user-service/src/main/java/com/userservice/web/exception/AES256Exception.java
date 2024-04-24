package com.userservice.web.exception;

public class AES256Exception extends RuntimeException {
    public AES256Exception() {
        super();
    }

    public AES256Exception(String message) {
        super(message);
    }

    public AES256Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public AES256Exception(Throwable cause) {
        super(cause);
    }
}
