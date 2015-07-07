package com.tchepannou.uds.dto;

import java.io.Serializable;

public class UserStatusRequest implements Serializable {
    //-- Attribute
    private long statusCode;
    private String comment;

    //-- Getter/Setter
    public long getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(long statusCode) {
        this.statusCode = statusCode;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
