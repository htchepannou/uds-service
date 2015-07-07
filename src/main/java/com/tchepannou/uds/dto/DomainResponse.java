package com.tchepannou.uds.dto;

import com.google.common.base.Preconditions;
import com.tchepannou.uds.domain.Domain;

import java.util.Date;

public class DomainResponse {
    //-- Attributes
    private final long id;
    private final String name;
    private final String description;
    private final Date fromDate;
    private final Date toDate;


    //-- Attributes
    private DomainResponse(final Builder builder) {
        final Domain domain = builder.domain;

        this.id = domain.getId();
        this.name = domain.getName();
        this.description = domain.getDescription();
        this.fromDate  = domain.getFromDate();
        this.toDate  = domain.getToDate();
    }

    //-- Getter/Setter
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    //-- Builder
    public static class Builder {
        private Domain domain;

        public DomainResponse build () {
            Preconditions.checkState(domain != null, "domain is null");

            return new DomainResponse(this);
        }

        public Builder withDomain (final Domain domain) {
            this.domain = domain;
            return this;
        }
    }
}
