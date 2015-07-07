package com.tchepannou.uds.dao;

import com.tchepannou.uds.domain.DomainUser;

import java.util.List;

public interface DomainUserDao {
    DomainUser findByDomainByUserByRole(long domainId, long userId, long roleId);

    List<DomainUser> findByDomainByUser(long domainId, long userId);

    long create(DomainUser domainUser);

    void delete(long id);
}
