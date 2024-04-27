package com.orderservice.domain.dto;

import lombok.Getter;

@Getter
public class ErrorDto {
    private final int status;
    private final String message;

    public ErrorDto(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
