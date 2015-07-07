package com.tchepannou.uds.service;

import com.tchepannou.uds.domain.PersistentEnum;

import java.util.List;

public interface AbstractPersistentEnumService<T extends PersistentEnum> {
    T findById(long id);

    T findByName(String name);

    List<T> findAll();
}
