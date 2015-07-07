package com.tchepannou.uds.service.impl;

import com.tchepannou.uds.dao.UserStatusCodeDao;
import com.tchepannou.uds.dto.UserStatusCodeListResponse;
import com.tchepannou.uds.service.UserStatusCodeService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserStatusCodeServiceImpl implements UserStatusCodeService {
    @Autowired
    private UserStatusCodeDao dao;

    public UserStatusCodeListResponse findAll () {
        return new UserStatusCodeListResponse.Builder()
                .withUserStatusCodes(
                        dao.findAll()
                )
                .build();
    }

}
