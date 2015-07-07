package com.tchepannou.uds.exception;

import java.io.Serializable;

public class NotFoundException extends RuntimeException {
    private final Serializable id;
    private final Class persistentClass;

    public NotFoundException(final Serializable id, final Class persistentClass) {
        super(persistentClass.getName() + "{" + id + "} not found");

        this.id = id;
        this.persistentClass = persistentClass;
    }

    public Serializable getId() {
        return id;
    }

    public Class getPersistentClass() {
        return persistentClass;
    }
}
