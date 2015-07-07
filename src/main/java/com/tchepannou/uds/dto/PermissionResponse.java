package com.tchepannou.uds.dto;

import com.google.common.base.Preconditions;
import com.tchepannou.uds.domain.Permission;

public class PermissionResponse {
    //-- Attributes
    private final long id;
    private final String name;

    //-- Constructor
    private PermissionResponse(Builder builder){
        Permission permission = builder.permission;
        this.id = permission.getId();
        this.name = permission.getName();
    }

    //-- Getter/Setter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class Builder {
        private Permission permission;

        public PermissionResponse build () {
            Preconditions.checkState(permission != null, "permission is not set");

            return new PermissionResponse(this);
        }

        public Builder withPermission (final Permission permission) {
            this.permission = permission;
            return this;
        }
    }
}
