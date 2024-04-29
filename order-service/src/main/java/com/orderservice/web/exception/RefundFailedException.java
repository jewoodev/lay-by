package com.orderservice.web.exception;

public class RefundFailedException extends RuntimeException {
    public RefundFailedException() {
        super();
    }

    public RefundFailedException(String message) {
        super(message);
    }

    public RefundFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefundFailedException(Throwable cause) {
        super(cause);
    }
}
