package com.tchepannou.uds.dao.impl;

import com.tchepannou.uds.dao.DomainUserDao;
import com.tchepannou.uds.domain.DomainUser;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DomainUserDaoImpl extends JdbcTemplate implements DomainUserDao {
    public DomainUserDaoImpl(final DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public DomainUser findByDomainByUserByRole(final long domainId, final long userId, final long roleId) {
        try {
            return queryForObject(
                    "SELECT * FROM t_domain_user WHERE domain_fk=? AND user_fk=? AND role_fk=?",
                    new Object[]{domainId, userId, roleId},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    public List<DomainUser> findByDomainByUser(long domainId, long userId) {
        return query(
                "SELECT * FROM t_domain_user WHERE domain_fk=? AND user_fk=?",
                new Object[]{domainId, userId},
                getRowMapper()
        );
    }

    @Override
    public long create(final DomainUser domainUser) {
        KeyHolder holder = new GeneratedKeyHolder();
        update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final String sql = "INSERT INTO t_domain_user(domain_fk, user_fk, role_fk, from_date) VALUES(?,?,?,?)";
                final PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                ps.setLong(1, domainUser.getDomainId());
                ps.setLong(2, domainUser.getUserId());
                ps.setLong(3, domainUser.getRoleId());
                ps.setTimestamp(4, DateUtils.asTimestamp(domainUser.getFromDate()));
                return ps;
            }
        }, holder);

        return holder.getKey().longValue();
    }

    @Override
    public void delete(final long id) {
        update("DELETE FROM t_domain_user WHERE id=?", new Object[] {id});
    }

    //-- Private
    private RowMapper<DomainUser> getRowMapper (){
        return new RowMapper<DomainUser>() {
            @Override
            public DomainUser mapRow(final ResultSet rs, final int i) throws SQLException {
                final DomainUser obj = new DomainUser ();
                obj.setId(rs.getLong("id"));

                obj.setDomainId(rs.getLong("domain_fk"));
                obj.setUserId(rs.getLong("user_fk"));
                obj.setRoleId(rs.getLong("role_fk"));

                obj.setFromDate(rs.getTimestamp("from_date"));
                return obj;
            }
        };
    }

}
