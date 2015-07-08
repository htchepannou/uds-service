package com.tchepannou.uds.dto;

import com.tchepannou.uds.domain.AccessToken;

import java.util.Date;

public class AccessTokenResponse {
    //-- Attributes
    private long id;
    private long userId;
    private long domainId;
    private Date fromDate;
    private Date toDate;
    private Date expiryDate;
    private boolean expired;


    //-- Constructor
    private AccessTokenResponse(final Builder builder){
        AccessToken token = builder.token;
        this.id = token.getId();
        this.userId = token.getUserId();
        this.domainId = token.getDomainId();
        this.expired = token.isExpired();
        this.fromDate = token.getFromDate();
        this.toDate = token.getToDate();
        this.expiryDate = token.getExpiryDate();
    }

    //-- Getter/Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public long getDomainId() {
        return domainId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public boolean isExpired() {
        return expired;
    }

    //-- Builder
    public static class Builder {
        private AccessToken token;

        public AccessTokenResponse build (){
            return new AccessTokenResponse(this);
        }

        public Builder withAccessToken (AccessToken token) {
            this.token = token;
            return this;
        }
    }
}
