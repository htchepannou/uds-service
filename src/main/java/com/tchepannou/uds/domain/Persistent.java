package com.tchepannou.uds.domain;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public abstract class Persistent implements Serializable{
    //-- Attribute
    private long id;

    //-- Getter/Setter
    public final long getId() {
        return id;
    }

    public final void setId(long id) {
        this.id = id;
    }

    //-- Object overrides
    @Override
    public boolean equals(final Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
