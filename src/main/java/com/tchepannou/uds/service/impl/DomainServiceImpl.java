package com.tchepannou.uds.service.impl;

import com.tchepannou.uds.dao.DomainDao;
import com.tchepannou.uds.domain.Domain;
import com.tchepannou.uds.dto.DomainRequest;
import com.tchepannou.uds.exception.DuplicateNameException;
import com.tchepannou.uds.exception.NotFoundException;
import com.tchepannou.uds.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public class DomainServiceImpl implements DomainService {
    //-- Attributes
    @Autowired
    private DomainDao domainDao;

    //-- DomainService overrides
    @Override
    public Domain findById(final long id) {
        Domain domain = domainDao.findById(id);
        if (domain == null || domain.isDeleted()) {
            throw new NotFoundException(id, Domain.class);
        }
        return domain;
    }

    @Override
    public List<Domain> findAll() {
        return domainDao.findAll();
    }

    @Override
    @Transactional
    public Domain create(final DomainRequest request) {
        try {
            final Domain domain = new Domain ();
            domain.setName(request.getName());
            domain.setDescription(request.getDescription());
            domain.setFromDate(new Date());

            domainDao.create(domain);
            return domain;
        } catch (DuplicateKeyException e) {
            throw new DuplicateNameException(request.getName(), e);
        }
    }

    @Override
    @Transactional
    public Domain update(final long id, final DomainRequest request) {
        try {
            Domain domain = findById(id);
            domain.setName(request.getName());
            domain.setDescription(request.getDescription());

            domainDao.update(domain);
            return domain;
        } catch (DuplicateKeyException e) {
            throw new DuplicateNameException(request.getName(), e);
        }
    }

    @Override
    @Transactional
    public void delete(final long id) {
        findById(id);       // Make sure that domain exists

        domainDao.delete(id);
    }
}
