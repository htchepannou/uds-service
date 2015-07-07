package com.tchepannou.uds.dao.impl;

import com.tchepannou.uds.dao.DomainDao;
import com.tchepannou.uds.domain.Domain;
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
import java.util.UUID;

public class DomainDaoImpl extends JdbcTemplate implements DomainDao {
    //-- Constructor
    public DomainDaoImpl(final DataSource ds) {
        super (ds);
    }

    //-- DomainDao overrides
    @Override
    public Domain findById (final long id) {
        try {
            return queryForObject(
                    "SELECT * FROM t_domain WHERE id=?",
                    new Object[]{id},
                    getRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {    // NOSONAR
            return null;
        }
    }

    @Override
    public List<Domain> findAll() {
        return query(
                "SELECT * FROM t_domain WHERE deleted=?",
                new Object[]{false},
                getRowMapper()
        );
    }

    @Override
    public long create(final Domain domain) {
        final KeyHolder holder = new GeneratedKeyHolder();
        update(new PreparedStatementCreator() {
                   @Override
                   public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                       final String sql = "INSERT INTO t_domain(name, description, from_date, deleted) VALUES(?,?,?,?)";
                       final PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                       ps.setString(1, domain.getName());
                       ps.setString(2, domain.getDescription());
                       ps.setTimestamp(3, DateUtils.asTimestamp(domain.getFromDate()));
                       ps.setBoolean(4, domain.isDeleted());
                       return ps;
                   }
               }, holder);

        long id = holder.getKey().longValue();
        domain.setId(id);
        return id;
    }

    @Override
    public void update(final Domain domain) {
        final String sql = "UPDATE t_domain SET name=?, description=? WHERE id=?";
        update(sql,
                domain.getName(),
                domain.getDescription(),
                domain.getId()
        );
    }

    @Override
    public void delete(final long id) {
        final String sql = "UPDATE t_domain SET deleted=?, name=?, to_date=? WHERE id=?";
        update(sql,
                true,
                UUID.randomUUID().toString(),
                new java.util.Date (),
                id
        );
    }

    //-- Private
    private RowMapper<Domain> getRowMapper (){
        return new RowMapper<Domain>() {
            @Override
            public Domain mapRow(final ResultSet rs, final int i) throws SQLException {
                final Domain obj = new Domain ();
                obj.setId(rs.getLong("id"));

                obj.setDeleted(rs.getBoolean("deleted"));
                obj.setFromDate(rs.getTimestamp("from_date"));
                obj.setToDate(rs.getTimestamp("to_date"));

                obj.setName(rs.getString("name"));
                obj.setDescription(rs.getString("description"));
                return obj;
            }
        };
    }
}
