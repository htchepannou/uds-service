package com.tchepannou.uds.domain;

import java.util.Date;

public class AccessToken extends Persistent {
    //-- Attributes
    private long userId;
    private long domainId;
    private Date fromDate;
    private Date toDate;
    private Date expiryDate;
    private boolean expired;
    private String remoteIp;
    private String userAgent;

    //-- Public
    public boolean expire (){
        if (!expired){
            expired = true;
            toDate = new Date ();
            return true;
        }
        return false;
    }

    //-- Getter/Setter
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDomainId() {
        return domainId;
    }

    public void setDomainId(long domainId) {
        this.domainId = domainId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isExpired() {
        return expired || (this.expiryDate != null && this.expiryDate.getTime() < System.currentTimeMillis());
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
