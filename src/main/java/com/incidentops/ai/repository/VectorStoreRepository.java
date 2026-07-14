package com.incidentops.ai.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class VectorStoreRepository {
    private final JdbcTemplate jdbcTemplate;
    public VectorStoreRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public void deleteByIncidentId(Long incidentId){
        String sql = """
                DELETE FROM vector_store
                WHERE metadata ->> 'incidentId' = ?
                """;
        jdbcTemplate.update(sql, incidentId.toString());
    }
}