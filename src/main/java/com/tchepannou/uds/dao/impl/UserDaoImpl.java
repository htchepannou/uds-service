package com.tchepannou.uds.dao.impl;

import com.tchepannou.uds.dao.UserDao;
import com.tchepannou.uds.domain.User;
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
import java.util.Date;
import java.util.UUID;

public class UserDaoImpl extends JdbcTemplate implements UserDao {
    public UserDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public User findById(final long id) {
        try {
            return queryForObject(
                    "SELECT U.* FROM t_user U WHERE U.id=?",
                    new Object[]{id},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    public User findByLogin(final String login) {
        try {
            return queryForObject(
                    "SELECT U.* FROM t_user U WHERE LOWER(U.login)=?",
                    new Object[]{login.toLowerCase()},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    public User findByParty(final long partyId) {
        try {
            return queryForObject(
                    "SELECT U.* FROM t_user U WHERE U.party_fk=?",
                    new Object[]{partyId},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    public long create(final User user) {
        user.setFromDate(new Date());

        KeyHolder holder = new GeneratedKeyHolder();
        update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final String sql = "INSERT INTO t_user(party_fk, login, password, from_date, deleted) VALUES(?,?,?,?,?)";
                final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});

                ps.setLong(1, user.getPartyId());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getPassword());
                ps.setTimestamp(4, DateUtils.asTimestamp(user.getFromDate()));
                ps.setBoolean(5, false);
                return ps;
            }
        }, holder);

        long id = holder.getKey().longValue();
        user.setId(id);
        return id;
    }

    @Override
    public void update(final User user) {
        final String sql = "UPDATE t_user SET status_fk=?, login=?, password=? WHERE id=?";
        update(sql,
                user.getStatusId(),
                user.getLogin(),
                user.getPassword(),
                user.getId()
        );
    }

    @Override
    public void delete(final long id) {
        final String sql = "UPDATE t_user SET deleted=?, login=?, to_date=? WHERE id=?";
        update(sql,
                true,
                UUID.randomUUID().toString(),
                new java.util.Date (),
                id
        );
    }

    private RowMapper<User> getRowMapper (){
        return new RowMapper<User>() {
            @Override
            public User mapRow(final ResultSet rs, final int i) throws SQLException {
                final User obj = new User ();
                obj.setId(rs.getLong("id"));
                obj.setPartyId(rs.getLong("party_fk"));
                obj.setStatusId(rs.getLong("status_fk"));

                obj.setDeleted(rs.getBoolean("deleted"));
                obj.setFromDate(rs.getTimestamp("from_date"));
                obj.setToDate(rs.getTimestamp("to_date"));

                obj.setLogin(rs.getString("login"));
                obj.setPassword(rs.getString("password"));
                return obj;
            }
        };
    }
}
