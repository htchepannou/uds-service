package com.tchepannou.uds.dao;

import com.tchepannou.core.dao.AbstractPersistentEnumDao;
import com.tchepannou.uds.domain.Permission;

import java.util.List;

public interface PermissionDao extends AbstractPersistentEnumDao<Permission> {
    List<Permission> findByRole(long roleId);
}
