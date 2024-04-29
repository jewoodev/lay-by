package com.itemservice.web.handler;

import com.itemservice.domain.dto.ErrorDto;
import com.itemservice.web.exception.DatabaseErrorException;
import com.itemservice.web.exception.NotEnoughStockException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { DatabaseErrorException.class, InternalServerErrorException.class })
    @ResponseBody
    protected ErrorDto interalServerError(RuntimeException ex, WebRequest request) {
        return new ErrorDto(INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(value = { NotEnoughStockException.class })
    @ResponseBody
    protected ErrorDto conflict(RuntimeException ex, WebRequest request) {
        return new ErrorDto(CONFLICT.value(), ex.getMessage());
    }
}
