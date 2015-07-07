package com.tchepannou.uds.domain;

import java.util.Date;

public class DomainUser extends Persistent {
    //-- Attribute
    private long domainId;
    private long userId;
    private long roleId;
    private Date fromDate;

    //-- Constructor
    public DomainUser(){

    }
    public DomainUser(final long domainId, final long userId, final long roleId) {
        this.domainId = domainId;
        this.userId = userId;
        this.roleId = roleId;
    }

    //-- Getter/Setter
    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
}
