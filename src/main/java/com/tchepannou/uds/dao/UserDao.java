package com.tchepannou.uds.dao;

import com.tchepannou.uds.domain.User;

public interface UserDao {
    User findById(long id);

    User findByLogin(String login);

    User findByParty(long partyId);

    long create(User user);

    void update(User user);

    void delete(long id);
}
