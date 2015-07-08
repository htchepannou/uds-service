package com.tchepannou.uds.exception;

public class AccessTokenExpiredException extends RuntimeException {
    public AccessTokenExpiredException(long id){
        super(String.valueOf(id));
    }
}
