package com.tchepannou.uds.dao.impl;

import com.tchepannou.uds.dao.AccessTokenDao;
import com.tchepannou.uds.domain.AccessToken;
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

public class AccessTokenDaoImpl extends JdbcTemplate implements AccessTokenDao {
    public AccessTokenDaoImpl(final DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public AccessToken findById(final long id) {
        try {
            return queryForObject(
                    "SELECT A.* FROM t_access_token A"
                            + " JOIN t_user U ON A.user_fk=U.id"
                            + " JOIN t_domain D on A.domain_fk=D.id"
                            + " WHERE A.id=? AND U.deleted=? AND D.deleted=?",
                    new Object[]{id, false, false},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    public long create(final AccessToken token) {
        KeyHolder holder = new GeneratedKeyHolder();
        update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                final String sql = "INSERT INTO t_access_token(user_fk, domain_fk, from_date, expiry_date, user_agent, remote_ip) VALUES(?,?,?,?,?,?)";
                final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});

                ps.setLong(1, token.getUserId());
                ps.setLong(2, token.getDomainId());
                ps.setTimestamp(3, DateUtils.asTimestamp(token.getFromDate()));
                ps.setTimestamp(4, DateUtils.asTimestamp(token.getExpiryDate()));
                ps.setString(5, token.getUserAgent());
                ps.setString(6, token.getRemoteIp());
                return ps;
            }
        }, holder);

        long id = holder.getKey().longValue();
        token.setId(id);
        return id;
    }

    @Override
    public void update(final AccessToken token) {
        String sql = "UPDATE t_access_token SET to_date=?, expiry_date=?, expired=?, user_agent=?, remote_ip=? WHERE id=?";
        update(sql,
                DateUtils.asTimestamp(token.getToDate()),
                DateUtils.asTimestamp(token.getExpiryDate()),
                token.isExpired(),
                token.getUserAgent(),
                token.getRemoteIp(),
                token.getId());
    }



    //-- Private
    private RowMapper<AccessToken> getRowMapper (){
        return new RowMapper<AccessToken>() {
            @Override
            public AccessToken mapRow(final ResultSet rs, final int i) throws SQLException {
                final AccessToken obj = new AccessToken ();
                obj.setId(rs.getLong("id"));
                obj.setUserId(rs.getLong("user_fk"));
                obj.setDomainId(rs.getLong("domain_fk"));

                obj.setFromDate(rs.getTimestamp("from_date"));
                obj.setToDate(rs.getTimestamp("to_date"));
                obj.setExpiryDate(rs.getTimestamp("expiry_date"));

                obj.setExpired(rs.getBoolean("expired"));
                obj.setUserAgent(rs.getString("user_agent"));
                obj.setRemoteIp(rs.getString("remote_ip"));
                return obj;
            }
        };
    }
}
