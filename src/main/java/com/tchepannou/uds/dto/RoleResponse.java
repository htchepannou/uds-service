package com.tchepannou.uds.dto;

import com.google.common.base.Preconditions;
import com.tchepannou.uds.domain.Role;

public class RoleResponse {
    //-- Attributes
    private final long id;
    private final String name;

    //-- Constructor
    private RoleResponse (Builder builder){
        Role role = builder.role;
        this.id = role.getId();
        this.name = role.getName();
    }

    //-- Getter/Setter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        private Role role;

        public RoleResponse build () {
            Preconditions.checkState(role != null, "role is not set");

            return new RoleResponse(this);
        }

        public Builder withRole (final Role role) {
            this.role = role;
            return this;
        }
    }
}
