package com.tchepannou.uds.dao;

import com.tchepannou.core.dao.AbstractPersistentEnumDao;
import com.tchepannou.uds.domain.UserStatusCode;

public interface UserStatusCodeDao extends AbstractPersistentEnumDao<UserStatusCode> {
    UserStatusCode findDefault();

    UserStatusCode findByUser (long userId);
}
