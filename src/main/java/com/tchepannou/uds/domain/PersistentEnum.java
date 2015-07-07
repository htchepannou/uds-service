package com.tchepannou.uds.domain;

public abstract class PersistentEnum extends Persistent {
    //-- Attributes
    private String name;

    //-- Getter/Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
