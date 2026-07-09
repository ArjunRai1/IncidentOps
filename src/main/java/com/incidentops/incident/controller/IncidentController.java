package com.incidentops.incident.controller;

import com.incidentops.incident.dto.CreateIncidentRequest;
import com.incidentops.incident.dto.IncidentResponse;
import com.incidentops.incident.dto.UpdateIncidentRequest;
import com.incidentops.incident.entity.IncidentPriority;
import com.incidentops.incident.entity.IncidentStatus;
import com.incidentops.incident.service.IncidentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/incidents")
@Validated
public class IncidentController {
    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody CreateIncidentRequest request){
        IncidentResponse response = incidentService.createIncident(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<IncidentResponse>> getAllIncidents(@RequestParam(defaultValue="0") @Min(0) int page, @RequestParam(defaultValue="10") @Min(1) @Max(50) int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "desc") String direction, @RequestParam(required = false) IncidentStatus status, @RequestParam(required = false) IncidentPriority priority, @RequestParam(required=false) String title){
        Page<IncidentResponse> response = incidentService.getAllIncidents(page, size, sortBy, direction, status, priority, title);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getIncidentById(@PathVariable Long id) {
        IncidentResponse response = incidentService.getIncidentById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncidentResponse> updateIncident(@Valid @RequestBody UpdateIncidentRequest request, @PathVariable Long id){
        IncidentResponse response = incidentService.updateIncident(request, id);
        return ResponseEntity.ok(response);
    }
}
