package com.orderservice.web.exception;

public class MailFailedException extends RuntimeException {
    public MailFailedException() {
        super();
    }

    public MailFailedException(String message) {
        super(message);
    }

    public MailFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailFailedException(Throwable cause) {
        super(cause);
    }
}
