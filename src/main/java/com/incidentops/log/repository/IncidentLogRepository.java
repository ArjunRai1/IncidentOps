package com.incidentops.log.repository;

import com.incidentops.log.entity.IncidentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentLogRepository extends JpaRepository<IncidentLog, Long> {
    List<IncidentLog> findByIncidentId(Long incidentId);
}
