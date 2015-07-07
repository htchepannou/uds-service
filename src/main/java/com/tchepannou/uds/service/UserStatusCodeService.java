package com.tchepannou.uds.service;

import com.tchepannou.uds.domain.UserStatusCode;

public interface UserStatusCodeService extends AbstractPersistentEnumService<UserStatusCode>{
    UserStatusCode findDefault();
}
