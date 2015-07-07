package com.tchepannou.uds.dto;

import com.google.common.base.Preconditions;
import com.tchepannou.uds.domain.User;
import com.tchepannou.uds.domain.UserStatus;
import com.tchepannou.uds.domain.UserStatusCode;

import java.io.Serializable;
import java.util.Date;

public class UserResponse implements Serializable {
    //-- Attribute
    private long id;
    private long partyId;
    private String login;
    private Date fromDate;
    private Date toDate;
    private UserStatusResponse status;

    //-- Constructor
    public UserResponse(final Builder builder){
        final User user = builder.user;

        this.id = user.getId();
        this.partyId = user.getPartyId();

        this.login = user.getLogin();
        this.fromDate = user.getFromDate();
        this.toDate = user.getToDate();

        if (builder.userStatus != null) {
            status = new UserStatusResponse.Builder()
                    .withUserStatus(builder.userStatus)
                    .withStatusCode(builder.userStatusCode)
                    .build();
        }
    }

    //-- Getter
    public long getId() {
        return id;
    }

    public long getPartyId() {
        return partyId;
    }

    public String getLogin() {
        return login;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public UserStatusResponse getStatus() {
        return status;
    }

    //-- Builder
    public static class Builder {
        private User user;
        private UserStatus userStatus;
        private UserStatusCode userStatusCode;

        public UserResponse build () {
            Preconditions.checkState(user != null, "user not set");

            return new UserResponse(this);
        }
        public Builder withUser (final User user) {
            this.user = user;
            return this;
        }
        public Builder withUserStatus (final UserStatus userStatus){
            this.userStatus = userStatus;
            return this;
        }
        public Builder withUserStatusCode (final UserStatusCode userStatusCode){
            this.userStatusCode = userStatusCode;
            return this;
        }
    }
}
