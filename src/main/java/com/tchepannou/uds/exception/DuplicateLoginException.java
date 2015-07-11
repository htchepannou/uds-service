package com.tchepannou.uds.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "duplicate_login")
public class DuplicateLoginException extends RuntimeException{
    public DuplicateLoginException(String name) {
        super (name);
    }
    public DuplicateLoginException(String name, Throwable cause) {
        super (name, cause);
    }
}
