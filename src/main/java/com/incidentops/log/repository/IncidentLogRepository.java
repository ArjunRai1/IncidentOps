package com.incidentops.log.repository;

import com.incidentops.log.entity.IncidentLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidentLogRepository extends JpaRepository<IncidentLog, Long> {
    List<IncidentLog> findByIncidentId(Long incidentId);
}
