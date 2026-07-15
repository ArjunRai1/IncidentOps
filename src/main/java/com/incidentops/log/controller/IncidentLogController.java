package com.incidentops.log.controller;

import com.incidentops.log.dto.LogUploadResponse;
import com.incidentops.log.service.IncidentLogService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/incidents/{incidentId}/logs")
public class IncidentLogController {
    private final IncidentLogService incidentLogService;

    public IncidentLogController(IncidentLogService incidentLogService) {
        this.incidentLogService = incidentLogService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LogUploadResponse> upload(@PathVariable Long incidentId, @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok(incidentLogService.uploadLog(incidentId, file));
    }
}
