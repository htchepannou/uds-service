package com.tchepannou.uds.dao;

import com.tchepannou.uds.domain.AccessToken;

public interface AccessTokenDao {
    AccessToken findById(long id);

    long create(AccessToken token);

    void update(AccessToken token);
}
