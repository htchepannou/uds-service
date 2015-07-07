package com.tchepannou.uds.dao;

import com.tchepannou.uds.domain.UserStatus;

import java.util.List;

public interface UserStatusDao {
    UserStatus findById(long id);
    List<UserStatus> findByUser(long id);
    long create(UserStatus userStatus);
}
