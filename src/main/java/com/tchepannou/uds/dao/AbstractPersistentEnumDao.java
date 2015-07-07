package com.tchepannou.uds.dao;

import com.tchepannou.uds.domain.PersistentEnum;

import java.util.List;

public interface AbstractPersistentEnumDao<T extends PersistentEnum> {
    T findById(long id);

    T findByName(String name);

    List<T> findAll();
}
