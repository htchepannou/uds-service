package com.tchepannou.uds.dto;

import com.tchepannou.uds.domain.Permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionListResponse {
    //-- Attributes
    private final List<PermissionResponse> permissions; // NOSONAR

    //-- Constructor
    private PermissionListResponse(Builder builder){
        final PermissionResponse.Builder permissionBuilder = new PermissionResponse.Builder();
        permissions = builder.permissions.stream()
                .map(permission -> permissionBuilder.withPermission(permission).build())
                .collect(Collectors.toList());
    }

    //-- Getter/Setter
    public List<PermissionResponse> getPermissions() {
        return Collections.unmodifiableList(permissions);
    }

    public static class Builder {
        private List<Permission> permissions = new ArrayList<>();

        public PermissionListResponse build () {
            return new PermissionListResponse(this);
        }

        public Builder withPermissions (final List<Permission> permissions) {
            this.permissions.addAll(permissions);
            return this;
        }
    }
}
