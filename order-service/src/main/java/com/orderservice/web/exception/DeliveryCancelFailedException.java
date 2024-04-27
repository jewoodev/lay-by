package com.orderservice.web.exception;

public class DeliveryCancelFailedException extends RuntimeException {
    public DeliveryCancelFailedException() {
        super();
    }

    public DeliveryCancelFailedException(String message) {
        super(message);
    }

    public DeliveryCancelFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeliveryCancelFailedException(Throwable cause) {
        super(cause);
    }
}
