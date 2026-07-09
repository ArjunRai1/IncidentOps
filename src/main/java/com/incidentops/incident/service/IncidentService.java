package com.incidentops.incident.service;

import com.incidentops.audit.entity.Action;
import com.incidentops.audit.service.AuditService;
import com.incidentops.auth.entity.User;
import com.incidentops.auth.repository.UserRepository;
import com.incidentops.incident.dto.CreateIncidentRequest;
import com.incidentops.incident.dto.IncidentResponse;
import com.incidentops.incident.dto.UpdateIncidentRequest;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.entity.IncidentPriority;
import com.incidentops.incident.entity.IncidentStatus;
import com.incidentops.incident.exception.IncidentNotFoundException;
import com.incidentops.incident.exception.InvalidStatusTransitionException;
import com.incidentops.incident.exception.UserNotFoundException;
import com.incidentops.incident.repository.IncidentRepository;
import com.incidentops.incident.specification.IncidentSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class IncidentService {
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;

    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository, AuditService auditService) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
    }

    public IncidentResponse createIncident(CreateIncidentRequest request) {
        User currentUser = getCurrentUser();
        Incident incident = new Incident();
        incident.setTitle(request.getTitle());
        incident.setDescription(request.getDescription());
        incident.setPriority(request.getPriority());
        incident.setStatus(IncidentStatus.OPEN);
        incident.setCreatedBy(currentUser);
        if (request.getAssignedTo() != null) {
            User assignee = userRepository.findById(request.getAssignedTo()).orElseThrow(() -> new UserNotFoundException());
            incident.setAssignedTo(assignee);
        }
        Incident savedIncident = incidentRepository.save(incident);
        auditService.log(savedIncident, currentUser, Action.INCIDENT_CREATED, "Incident created");
        IncidentResponse mappedResponse = mapToResponse(savedIncident);
        return mappedResponse;
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
    }

    private IncidentResponse mapToResponse(Incident incident){
        IncidentResponse response = new IncidentResponse();
        response.setTitle(incident.getTitle());
        response.setDescription(incident.getDescription());
        response.setPriority(incident.getPriority());
        response.setStatus(incident.getStatus());
        response.setCreatedById(incident.getCreatedBy().getId());
        response.setCreatedAt(incident.getCreatedAt());
        response.setUpdatedAt(incident.getUpdatedAt());
        response.setId(incident.getId());
        response.setCreatedBy(incident.getCreatedBy().getUsername());
        if(incident.getAssignedTo()!=null) {
            response.setAssignedToId(incident.getAssignedTo().getId());
            response.setAssignedTo(incident.getAssignedTo().getUsername());
        }
        return response;
    }
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "createdAt",
            "updatedAt",
            "priority",
            "status",
            "title"
    );

    private void validateStatusTransition(IncidentStatus current, IncidentStatus next){
        switch(current){
            case OPEN:
                if(next != IncidentStatus.IN_PROGRESS){
                    throw new InvalidStatusTransitionException();
                }
                break;
            case IN_PROGRESS:
                if(next != IncidentStatus.TESTING) {
                    throw new InvalidStatusTransitionException();
                }
                break;
            case TESTING:
                if(next != IncidentStatus.IN_PROGRESS && next != IncidentStatus.CLOSED){
                    throw new InvalidStatusTransitionException();
                }
                break;
            case CLOSED:
                if(next != IncidentStatus.OPEN){
                    throw new InvalidStatusTransitionException();
                }
                break;
        }
    }

    public Page<IncidentResponse> getAllIncidents(int page, int size, String sortBy, String direction, IncidentStatus status, IncidentPriority priority, String title){
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field");
        }
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Invalid direction for sort");
        }
        Sort sort = (direction.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        //Building query fragments using specification
        Specification<Incident> specification = Specification.unrestricted();

        //Resembles and clause in normal sql query(and status="...")
        if (status != null) {
            specification = specification.and(IncidentSpecification.hasStatus(status));
        }

        if (priority != null) {
            specification = specification.and(IncidentSpecification.hasPriority(priority));
        }

        if(title!=null){
            specification = specification.and(IncidentSpecification.hasTitle(title));
        }

        Page<Incident> incidents = incidentRepository.findAll(specification, pageable);
        return incidents.map(this::mapToResponse);
    }

    public IncidentResponse getIncidentById(Long id) {
        Incident incident = incidentRepository.findById(id).orElseThrow(()-> new IncidentNotFoundException());
        return mapToResponse(incident);
    }

    public IncidentResponse updateIncident(UpdateIncidentRequest request, Long id){
        boolean detailsUpdated = false;
        User currentUser = getCurrentUser();
        Incident incident = incidentRepository.findById(id).orElseThrow(()-> new IncidentNotFoundException());
        if (request.getTitle() != null) {
            incident.setTitle(request.getTitle());
            detailsUpdated = true;
        }

        if (request.getDescription() != null) {
            incident.setDescription(request.getDescription());
            detailsUpdated = true;
        }

        if(request.getPriority() != null && request.getPriority() != incident.getPriority()){
            IncidentPriority oldPriority = incident.getPriority();
            incident.setPriority(request.getPriority());
            auditService.log(incident, currentUser, Action.PRIORITY_CHANGED, "Priority changed from " + oldPriority + " to " + request.getPriority());
        }

        if(request.getAssignedTo() != null){
            User assignee = userRepository.findById(request.getAssignedTo()).orElseThrow(() -> new UserNotFoundException());
            incident.setAssignedTo(assignee);
            auditService.log(incident, currentUser, Action.ASSIGNEE_CHANGED, "Assigned to " + assignee.getEmail());
        }

        if (request.getStatus() != null && request.getStatus() != incident.getStatus()) {
            validateStatusTransition(incident.getStatus(), request.getStatus());
            IncidentStatus oldStatus = incident.getStatus();
            incident.setStatus(request.getStatus());
            auditService.log(incident, currentUser, Action.STATUS_CHANGED, "Status changed from " + oldStatus + " to " + request.getStatus());
        }
        if(detailsUpdated){
            auditService.log(incident, currentUser, Action.INCIDENT_UPDATED, "Incident details updated");
        }
        return mapToResponse(incidentRepository.save(incident));
    }
}
