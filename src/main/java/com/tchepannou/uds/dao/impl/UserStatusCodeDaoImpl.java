package com.tchepannou.uds.dao.impl;

import com.tchepannou.core.dao.impl.AbstractPersistentEnumDaoImpl;
import com.tchepannou.uds.dao.UserStatusCodeDao;
import com.tchepannou.uds.domain.UserStatusCode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserStatusCodeDaoImpl extends AbstractPersistentEnumDaoImpl<UserStatusCode> implements UserStatusCodeDao {

    public UserStatusCodeDaoImpl(final DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public UserStatusCode findDefault() {
        return queryForObject(
                "SELECT * FROM " + getTableName() + " WHERE default_status=?",
                new Object[]{true},
                getRowMapper()
        );
    }

    @Override
    public UserStatusCode findByUser (long userId) {
        try {
            return queryForObject(
                    "SELECT * FROM " + getTableName() + " SC"
                            + " JOIN t_user_status S ON S.status_code_fk=SC.id"
                            + " JOIN t_user U ON U.status_fk=S.id"
                            + " WHERE U.id=?",
                    new Object[]{userId},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    protected String getTableName() {
        return "t_user_status_code";
    }

    @Override
    protected UserStatusCode createPersistenEnum() {
        return new UserStatusCode();
    }

    @Override
    protected RowMapper<UserStatusCode> getRowMapper () {
        return new RowMapper<UserStatusCode>() {
            @Override
            public UserStatusCode mapRow(final ResultSet rs, final int i) throws SQLException {
                final UserStatusCode obj = createPersistenEnum();
                obj.setId(rs.getLong("id"));
                obj.setName(rs.getString("name"));
                obj.setActive(rs.getBoolean("active"));
                obj.setDefaultStatus(rs.getBoolean("default_status"));
                return obj;
            }
        };
    }

}
