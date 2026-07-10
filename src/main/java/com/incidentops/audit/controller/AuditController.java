package com.incidentops.audit.controller;

import com.incidentops.audit.dto.AuditResponse;
import com.incidentops.audit.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/incidents/{incidentId}/timeline")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService){
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<AuditResponse>> getTimeline(@PathVariable Long incidentId){
        List<AuditResponse> response = auditService.getTimeline(incidentId);
        return ResponseEntity.ok(response);
    }
}
