package com.incidentops.incident.specification;

import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.entity.IncidentPriority;
import com.incidentops.incident.entity.IncidentStatus;
import org.springframework.data.jpa.domain.Specification;

public final class IncidentSpecification {
    private IncidentSpecification() {}
    public static Specification<Incident> hasStatus(IncidentStatus status) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Incident> hasPriority(IncidentPriority priority) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }
}
