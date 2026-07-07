package com.incidentops.incident.service;

import com.incidentops.auth.entity.User;
import com.incidentops.auth.repository.UserRepository;
import com.incidentops.incident.dto.CreateIncidentRequest;
import com.incidentops.incident.dto.IncidentResponse;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.entity.IncidentStatus;
import com.incidentops.incident.exception.UserNotFoundException;
import com.incidentops.incident.repository.IncidentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class IncidentService {
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
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
    public Page<IncidentResponse> getAllIncidents(int page, int size, String sortBy, String direction){
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sort field");
        }
        if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
            throw new IllegalArgumentException("Invalid direction for sort");
        }
        Sort sort = (direction.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Incident> incidents = incidentRepository.findAll(pageable);
        return incidents.map(this::mapToResponse);
    }
}
