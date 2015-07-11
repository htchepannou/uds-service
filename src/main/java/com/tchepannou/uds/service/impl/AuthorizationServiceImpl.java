package com.tchepannou.uds.service.impl;

import com.tchepannou.uds.dao.DomainUserDao;
import com.tchepannou.uds.dao.PermissionDao;
import com.tchepannou.uds.dao.RoleDao;
import com.tchepannou.uds.domain.DomainUser;
import com.tchepannou.uds.domain.Permission;
import com.tchepannou.uds.domain.Role;
import com.tchepannou.uds.dto.PermissionListResponse;
import com.tchepannou.uds.dto.RoleListResponse;
import com.tchepannou.uds.exception.NotFoundException;
import com.tchepannou.uds.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorizationServiceImpl implements AuthorizationService {
    //-- Attributes
    @Autowired
    private DomainUserDao domainUserDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    //-- AuthorizationService
    @Override
    public PermissionListResponse permissions (long domainId, long userId){
        final List<DomainUser> domainUsers = domainUserDao.findByDomainByUser(domainId, userId);

        final List<Role> roles = domainUsers.stream()
                .map(domainUser -> roleDao.findById(domainUser.getRoleId()))
                .filter(role -> role != null)
                .distinct()
                .collect(Collectors.toList());

        final List<Permission> permissions = roles.stream()
                .flatMap(role -> permissionDao.findByRole(role.getId()).stream())
                .filter(permission ->  permission != null)
                .distinct()
                .collect(Collectors.toList());

        return new PermissionListResponse.Builder()
                .withPermissions(permissions)
                .build();
    }

    @Override
    public RoleListResponse roles(long domainId, long userId) {
        final List<DomainUser> domainUsers = domainUserDao.findByDomainByUser(domainId, userId);
        final List<Role> roles = domainUsers.stream()
                .map(domainUser -> roleDao.findById(domainUser.getRoleId()))
                .filter(role -> role != null)
                .distinct()
                .collect(Collectors.toList())
        ;

        return new RoleListResponse.Builder()
                .withRoles(roles)
                .build();
    }

    @Override
    @Transactional
    public void grant(
            final long domainId,
            final long userId,
            final long roleId
    ) {
        try {
            DomainUser domainUser = new DomainUser(domainId, userId, roleId);
            domainUserDao.create(domainUser);
        } catch (DuplicateKeyException e) { // NOSONAR
        } catch (DataIntegrityViolationException e) {
            ArrayList<Long> keys = new ArrayList<>();
            keys.add(domainId);
            keys.add(userId);
            keys.add(roleId);
            throw new NotFoundException(keys, DomainUser.class);
        }
    }

    @Override
    @Transactional
    public void revoke(
            final long domainId,
            final long userId,
            final long roleId
    ) {
        DomainUser domainUser = domainUserDao.findByDomainByUserByRole(domainId, userId, roleId);
        if (domainUser != null) {
            domainUserDao.delete(domainUser.getId());
        }
    }
}
