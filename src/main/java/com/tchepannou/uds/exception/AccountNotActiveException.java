package com.tchepannou.uds.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException (String message){
        super (message);
    }
}
