package com.tchepannou.uds.dto;

import com.google.common.base.Preconditions;
import com.tchepannou.uds.domain.UserStatus;
import com.tchepannou.uds.domain.UserStatusCode;

import java.io.Serializable;
import java.util.Date;

public class UserStatusResponse implements Serializable {
    //-- Attribute
    private long id;
    private long statusCode;
    private String statusText;
    private String comment;
    private Date date;

    //-- Constructor
    public UserStatusResponse(final Builder builder){
        final UserStatus userStatus = builder.userStatus;
        final UserStatusCode userStatusCode = builder.statusCode;

        this.id = userStatus.getId();
        this.statusCode = userStatusCode.getId();
        this.statusText = userStatusCode.getName();
        this.date = userStatus.getDate();
        this.comment = userStatus.getComment();
    }

    //-- Getter
    public long getId() {
        return id;
    }

    public long getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public String getComment() {
        return comment;
    }

    public Date getDate() {
        return date;
    }

    //-- Builder
    public static class Builder {
        private UserStatus userStatus;
        private UserStatusCode statusCode;

        public UserStatusResponse build () {
            Preconditions.checkState(userStatus != null, "userStatus not set");
            Preconditions.checkState(statusCode != null, "userStatusCode not set");
            Preconditions.checkState(statusCode.getId() == userStatus.getStatusCodeId(), "userStatusCode is invalid");

            return new UserStatusResponse(this);
        }
        
        public Builder withUserStatus(final UserStatus userStatus) {
            this.userStatus = userStatus;
            return this;
        }
        
        public Builder withStatusCode (final UserStatusCode userStatusCode) {
            this.statusCode = userStatusCode;
            return this;
        }
    }
}
