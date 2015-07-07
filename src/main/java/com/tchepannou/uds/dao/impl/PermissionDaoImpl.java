package com.tchepannou.uds.dao.impl;

import com.tchepannou.uds.dao.PermissionDao;
import com.tchepannou.uds.domain.Permission;

import javax.sql.DataSource;
import java.util.List;

public class PermissionDaoImpl extends AbstractPersistentEnumDaoImpl<Permission> implements PermissionDao {

    public PermissionDaoImpl(final DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getTableName() {
        return "t_permission";
    }

    @Override
    protected Permission createPersistenEnum() {
        return new Permission();
    }

    @Override
    public List<Permission> findByRole(final long roleId) {
        return query(
                "SELECT P.* FROM t_permission P JOIN t_role_permission R ON P.id=R.permission_fk WHERE role_fk=?",
                new Object[] {roleId},
                getRowMapper());
    }
}
