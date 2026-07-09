package com.incidentops.audit.repository;

import com.incidentops.audit.entity.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditRepository extends JpaRepository<Audit, Long> {
    List<Audit> findByIncidentIdOrderByCreatedAtDesc(Long incidentId);
}
