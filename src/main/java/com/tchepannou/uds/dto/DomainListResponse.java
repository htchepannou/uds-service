package com.tchepannou.uds.dto;

import com.google.common.base.Preconditions;
import com.tchepannou.uds.domain.Domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DomainListResponse {
    //-- Attributes
    private final List<DomainResponse> domains;


    //-- Attributes
    private DomainListResponse(final Builder builder) {
        final DomainResponse.Builder domainBuilder = new DomainResponse.Builder();
        this.domains = builder.domains.stream()
                .map(domain -> domainBuilder.withDomain(domain).build())
                .collect(Collectors.toList());
    }

    //-- Getter/Setter
    public List<DomainResponse> getDomains() {
        return domains;
    }

    //-- Builder
    public static class Builder {
        private List<Domain> domains;

        public DomainListResponse build () {
            Preconditions.checkState(domains != null, "domains not set");

            return new DomainListResponse(this);
        }

        public Builder withDomains(final List<Domain> domains) {
            this.domains = new ArrayList<>(domains);
            return this;
        }
    }
}
