package com.tchepannou.uds.dto;

import com.wordnik.swagger.annotations.ApiModel;
import org.springframework.http.HttpStatus;

import java.util.Date;

@ApiModel
public class ErrorResponse {
    //-- Attribute
    private int statusCode;
    private final String message;
    private final Date timestamp;

    //-- Attributes
    public ErrorResponse(final HttpStatus status, final String message){
        this.timestamp = new Date();
        this.statusCode = status.value();
        this.message = message;
    }

    //-- Getter/

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
