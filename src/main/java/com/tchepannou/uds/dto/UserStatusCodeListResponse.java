package com.tchepannou.uds.dto;

import com.tchepannou.uds.domain.UserStatusCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserStatusCodeListResponse {
    //-- Attributes
    private final List<UserStatusCodeResponse> statuses;

    //-- Constructor
    private UserStatusCodeListResponse(final Builder builder){
        final UserStatusCodeResponse.Builder statusBuilder = new UserStatusCodeResponse.Builder();
        statuses = builder.statuses.stream()
                .map(status -> statusBuilder.withUserStatusCode(status).build())
                .collect(Collectors.toList());
    }

    //-- Getter/Setter
    public List<UserStatusCodeResponse> getUserStatusCodes() {
        return Collections.unmodifiableList(statuses);
    }

    public static class Builder {
        private List<UserStatusCode> statuses = new ArrayList<>();

        public UserStatusCodeListResponse build () {
            return new UserStatusCodeListResponse(this);
        }

        public Builder withUserStatusCodes (final List<UserStatusCode> statuses) {
            this.statuses = Collections.unmodifiableList(statuses);
            return this;
        }
    }
}
