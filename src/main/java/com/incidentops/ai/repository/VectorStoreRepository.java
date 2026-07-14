package com.incidentops.ai.repository;

import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class VectorStoreRepository {
    private final JdbcTemplate jdbcTemplate;
    public VectorStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void deleteByIncidentId(Long incidentId) throws SQLException {
        String sql = """
                DELETE FROM vector_store
                WHERE metadata ->> 'incidentId' = ?
                """;
        jdbcTemplate.update(sql, incidentId.toString());
    }
}