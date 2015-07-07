package com.tchepannou.uds.exception;

public class DuplicatePartyException extends RuntimeException{
    public DuplicatePartyException(long id) {
        super(String.valueOf(id));
    }
}
