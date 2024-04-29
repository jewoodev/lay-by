package com.orderservice.web.exception;

public class CertificationFailedException extends RuntimeException {
    public CertificationFailedException() {
        super();
    }

    public CertificationFailedException(String message) {
        super(message);
    }

    public CertificationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CertificationFailedException(Throwable cause) {
        super(cause);
    }
}
