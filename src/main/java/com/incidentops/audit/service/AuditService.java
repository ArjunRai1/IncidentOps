package com.incidentops.audit.service;

import com.incidentops.audit.dto.AuditResponse;
import com.incidentops.audit.entity.Action;
import com.incidentops.audit.entity.Audit;
import com.incidentops.audit.repository.AuditRepository;
import com.incidentops.auth.entity.User;
import com.incidentops.comment.exception.IncidentDoesNotExistException;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {
    private final AuditRepository auditRepository;
    private final IncidentRepository incidentRepository;

    public AuditService(AuditRepository auditRepository, IncidentRepository incidentRepository) {
        this.auditRepository = auditRepository;
        this.incidentRepository = incidentRepository;
    }

    private AuditResponse mapToResponse(Audit audit){
        AuditResponse response = new AuditResponse();
        response.setId(audit.getId());
        response.setAction(audit.getAction());
        response.setDescription(audit.getDescription());
        response.setEmail(audit.getUser().getEmail());
        response.setCreatedAt(audit.getCreatedAt());
        return response;
    }

    public void log(Incident incident, User user, Action action, String description){
        Audit audit = new Audit();
        audit.setIncident(incident);
        audit.setUser(user);
        audit.setAction(action);
        audit.setDescription(description);
        auditRepository.save(audit);
    }

    public List<AuditResponse> getTimeline(Long incidentId){
        incidentRepository.findById(incidentId).orElseThrow(()->new IncidentDoesNotExistException());
        List<Audit> auditList = auditRepository.findByIncidentIdOrderByCreatedAtDesc(incidentId);
        return auditList.stream().map(this::mapToResponse).toList();
    }
}
