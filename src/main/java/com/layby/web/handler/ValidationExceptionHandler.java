package com.layby.web.handler;

import com.layby.domain.dto.response.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler({ MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ResponseDto> validationExceptionHandler(Exception e) {
        return ResponseDto.validationFail();
    }
}
