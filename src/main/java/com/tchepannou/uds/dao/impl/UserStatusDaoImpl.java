package com.tchepannou.uds.dao.impl;

import com.tchepannou.uds.dao.UserStatusDao;
import com.tchepannou.uds.domain.UserStatus;
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
import java.util.List;

public class UserStatusDaoImpl extends JdbcTemplate implements UserStatusDao {
    public UserStatusDaoImpl(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public UserStatus findById(long id) {
        try {
            return queryForObject(
                    "SELECT * FROM t_user_status WHERE id=?",
                    new Object[]{id},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    public List<UserStatus> findByUser(long id) {
        return query(
                "SELECT * FROM t_user_status WHERE user_fk=?",
                new Object[]{id},
                getRowMapper()
        );
    }

    @Override
    public long create(UserStatus userStatus) {
        userStatus.setDate(new Date());
        final KeyHolder holder = new GeneratedKeyHolder();
        update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final String sql = "INSERT INTO t_user_status(user_fk, status_code_fk, status_date, comment) VALUES(?,?,?,?)";
                final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});

                ps.setLong(1, userStatus.getUserId());
                ps.setLong(2, userStatus.getStatusCodeId());
                ps.setTimestamp(3, DateUtils.asTimestamp(userStatus.getDate()));
                ps.setString(4, userStatus.getComment());

                return ps;
            }
        }, holder);

        long id = holder.getKey().longValue();
        userStatus.setId(id);
        return id;
    }

    private RowMapper<UserStatus> getRowMapper(){
        return new RowMapper<UserStatus>() {
            @Override
            public UserStatus mapRow(ResultSet resultSet, int i) throws SQLException {
                UserStatus obj = new UserStatus();
                obj.setId(resultSet.getLong("id"));
                obj.setUserId(resultSet.getLong("user_fk"));
                obj.setStatusCodeId(resultSet.getLong("status_code_fk"));
                obj.setComment(resultSet.getString("comment"));
                obj.setDate(resultSet.getTimestamp("status_date"));
                return obj;
            }
        };
    }
}
