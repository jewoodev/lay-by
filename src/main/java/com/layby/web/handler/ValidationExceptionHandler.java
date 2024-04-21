package com.layby.web.handler;

import com.layby.domain.dto.ErrorDto;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.layby.domain.common.ErrorCode.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ErrorDto validationExceptionHandler(Exception e) {
        return new ErrorDto(UNAUTHORIZED.value(), VALIDATION_FAIL.getMessage());
    }
}
