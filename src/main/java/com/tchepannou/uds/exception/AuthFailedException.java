package com.tchepannou.uds.exception;

public class AuthFailedException extends RuntimeException {
    public AuthFailedException(String message){
        super(message);
    }
    public AuthFailedException(String message, Throwable cause){
        super(message, cause);
    }
}
