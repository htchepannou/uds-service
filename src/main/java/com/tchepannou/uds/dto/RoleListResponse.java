package com.tchepannou.uds.dto;

import com.tchepannou.uds.domain.Role;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoleListResponse {
    //-- Attributes
    private final List<RoleResponse> roles;

    //-- Constructor
    private RoleListResponse(Builder builder){
        final RoleResponse.Builder roleBuilder = new RoleResponse.Builder();
        roles = builder.roles.stream()
                .map(role -> roleBuilder.withRole(role).build())
                .collect(Collectors.toList());
    }

    //-- Getter/Setter
    public List<RoleResponse> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public static class Builder {
        private List<Role> roles = new ArrayList<>();

        public RoleListResponse build () {
            return new RoleListResponse(this);
        }

        public Builder withRoles (final List<Role> roles) {
            this.roles.addAll(roles);
            return this;
        }
    }
}
