package com.tchepannou.uds.service;

import com.tchepannou.uds.dto.PermissionListResponse;
import com.tchepannou.uds.dto.RoleListResponse;

public interface AuthorizationService {
    PermissionListResponse permissions (long domainId, long userId);

    RoleListResponse roles (long domainId, long userId);
    
    void grant (long domainId, long userId, long roleId);

    void revoke (long domainId, long userId, long roleId);
}
