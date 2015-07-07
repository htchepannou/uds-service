package com.tchepannou.uds.domain;

import java.util.Date;

public class UserStatus extends Persistent{
    //-- Attribute
    private long userId;
    private long statusCodeId;
    private String comment;
    private Date date;


    //-- Getter/Setter
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getStatusCodeId() {
        return statusCodeId;
    }

    public void setStatusCodeId(long statusCodeId) {
        this.statusCodeId = statusCodeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
