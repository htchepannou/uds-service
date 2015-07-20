package com.tchepannou.uds.dto;

import com.google.common.base.Preconditions;
import com.tchepannou.uds.domain.UserStatusCode;

public class UserStatusCodeResponse {
    private long id;
    private String name;
    private boolean active;
    private boolean defaultStatus;

    //-- Constructor
    private UserStatusCodeResponse (final Builder builder){
        UserStatusCode status = builder.role;
        this.id = status.getId();
        this.name = status.getName();
        this.active = status.isActive();
        this.defaultStatus = status.isDefaultStatus();
    }

    //-- Getter/Setter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isDefaultStatus() {
        return defaultStatus;
    }

    public static class Builder {
        private UserStatusCode role;

        public UserStatusCodeResponse build () {
            Preconditions.checkState(role != null, "role is not set");

            return new UserStatusCodeResponse(this);
        }

        public Builder withUserStatusCode (final UserStatusCode role) {
            this.role = role;
            return this;
        }
    }
}
