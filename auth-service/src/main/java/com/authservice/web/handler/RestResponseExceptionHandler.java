package com.authservice.web.handler;

import com.authservice.domain.dto.ErrorDto;
import com.authservice.web.exception.*;
import org.springframework.security.access.AccessDeniedException;
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
    @ExceptionHandler(value = { AES256Exception.class, DatabaseErrorException.class,
            InternalServerErrorException.class })
    @ResponseBody
    protected ErrorDto interalServerError(RuntimeException ex, WebRequest request) {
        return new ErrorDto(INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(value = { AccessDeniedException.class })
    @ResponseBody
    protected ErrorDto forbidden(RuntimeException ex, WebRequest request) {
        return new ErrorDto(FORBIDDEN.value(), ex.getMessage());
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(value = { SignInFailedException.class, CertificationFailedException.class })
    @ResponseBody
    protected ErrorDto unauthorized(RuntimeException ex, WebRequest request) {
        return new ErrorDto(UNAUTHORIZED.value(), ex.getMessage());
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(value = { DuplicatedUsernameException.class, MailFailedException.class })
    @ResponseBody
    protected ErrorDto badRequest(RuntimeException ex, WebRequest request) {
        return new ErrorDto(BAD_REQUEST.value(), ex.getMessage());
    }
}
