package com.tchepannou.uds.dao;

import com.tchepannou.uds.domain.Domain;

import java.util.List;

public interface DomainDao {
    Domain findById(long id);

    List<Domain> findAll();

    long create(Domain domain);

    void update(Domain domain);

    void delete(long id);
}
