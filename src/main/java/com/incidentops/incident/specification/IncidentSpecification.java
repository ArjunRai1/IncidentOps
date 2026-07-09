package com.incidentops.incident.specification;

import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.entity.IncidentPriority;
import com.incidentops.incident.entity.IncidentStatus;
import org.springframework.data.jpa.domain.Specification;

public final class IncidentSpecification {
    private IncidentSpecification() {}
    public static Specification<Incident> hasStatus(IncidentStatus status) {
        //Resembles sql query Checking status="status passed in function"
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Incident> hasPriority(IncidentPriority priority) {
        //Resembles sql query Checking priority="priority passed in function"
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Incident> hasTitle(String title){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }
}
