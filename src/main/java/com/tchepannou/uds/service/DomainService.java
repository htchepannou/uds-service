package com.tchepannou.uds.service;

import com.tchepannou.uds.dto.DomainListResponse;
import com.tchepannou.uds.dto.DomainRequest;
import com.tchepannou.uds.dto.DomainResponse;

public interface DomainService {
    DomainResponse findById(long id);

    DomainListResponse findAll();

    DomainResponse create(DomainRequest request);

    DomainResponse update(long id, DomainRequest domain);

    void delete(long id);
}
