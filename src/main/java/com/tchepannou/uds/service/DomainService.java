package com.tchepannou.uds.service;

import com.tchepannou.uds.domain.Domain;
import com.tchepannou.uds.dto.DomainRequest;

import java.util.List;

public interface DomainService {
    Domain findById(long id);

    List<Domain> findAll();

    Domain create(DomainRequest request);

    Domain update(long id, DomainRequest domain);

    void delete(long id);
}
