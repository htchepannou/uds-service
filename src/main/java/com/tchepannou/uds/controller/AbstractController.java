package com.tchepannou.uds.controller;

import com.google.common.base.Joiner;
import com.tchepannou.uds.dto.ErrorResponse;
import com.tchepannou.uds.exception.NotFoundException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractController {
    //-- Abstract methods
    protected abstract Logger getLogger ();

    //-- Error Handlers
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final NotFoundException exception
    ) {
        return handleException(request, HttpStatus.NOT_FOUND, "not_found", exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    public ErrorResponse handleError(
            final HttpServletRequest request,
            final MethodArgumentNotValidException exception
    ) {
        final List<ObjectError> errors = exception.getBindingResult().getAllErrors();
        final String message = Joiner
                .on(',')
                .join(
                    errors
                            .stream()
                            .map(error -> error.getDefaultMessage())
                            .collect(Collectors.toList())
                );

        return handleException(request, HttpStatus.BAD_REQUEST, message, exception);
    }

    protected ErrorResponse handleException(
            final HttpServletRequest request,
            final HttpStatus status,
            final String message,
            final Exception exception
    ) {
        getLogger().error("{} {}{}",
                request.getMethod(),
                request.getRequestURI(),
                request.getQueryString() != null ? "?"+request.getQueryString() : "",
                exception);

        return new ErrorResponse(status, message);
    }
}
