package com.tchepannou.uds.dao.impl;

import com.tchepannou.core.dao.impl.AbstractPersistentEnumDaoImpl;
import com.tchepannou.uds.dao.RoleDao;
import com.tchepannou.uds.domain.Role;

import javax.sql.DataSource;

public class RoleDaoImpl extends AbstractPersistentEnumDaoImpl<Role> implements RoleDao {

    public RoleDaoImpl(final DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "t_role";
    }

    @Override
    protected Role createPersistenEnum() {
        return new Role();
    }
}
