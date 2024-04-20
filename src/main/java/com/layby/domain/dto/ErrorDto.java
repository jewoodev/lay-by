package com.layby.domain.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import org.springframework.validation.FieldError;

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
