package com.tchepannou.uds.service.impl;

import com.tchepannou.uds.dao.AbstractPersistentEnumDao;
import com.tchepannou.uds.dao.UserStatusCodeDao;
import com.tchepannou.uds.domain.UserStatusCode;
import com.tchepannou.uds.service.UserStatusCodeService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserStatusCodeServiceImpl extends AbstractPersistentEnumServiceImpl<UserStatusCode> implements UserStatusCodeService {
    @Autowired
    private UserStatusCodeDao dao;

    @Override
    public UserStatusCode findDefault() {
        return ((UserStatusCodeDao)getDao()).findDefault();
    }

    @Override
    protected AbstractPersistentEnumDao<UserStatusCode> getDao() {
        return dao;
    }
}
