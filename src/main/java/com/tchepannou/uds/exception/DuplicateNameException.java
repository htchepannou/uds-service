package com.tchepannou.uds.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "duplicate_name")
public class DuplicateNameException extends RuntimeException{
    public DuplicateNameException(String name, Exception e) {
        super (name, e);
    }
}
