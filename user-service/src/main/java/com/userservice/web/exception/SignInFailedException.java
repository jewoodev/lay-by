package com.userservice.web.exception;

public class SignInFailedException extends RuntimeException {
    public SignInFailedException() {
        super();
    }

    public SignInFailedException(String message) {
        super(message);
    }

    public SignInFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SignInFailedException(Throwable cause) {
        super(cause);
    }
}
