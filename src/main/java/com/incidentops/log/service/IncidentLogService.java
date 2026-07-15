package com.incidentops.log.service;

import com.incidentops.ai.indexing.LogIndexingService;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.exception.IncidentNotFoundException;
import com.incidentops.incident.repository.IncidentRepository;
import com.incidentops.log.dto.LogUploadResponse;
import com.incidentops.log.entity.IncidentLog;
import com.incidentops.log.repository.IncidentLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class IncidentLogService {
    private final IncidentRepository incidentRepository;
    private final IncidentLogRepository incidentLogRepository;
    private final LogIndexingService logIndexingService;


    public IncidentLogService(IncidentRepository incidentRepository, IncidentLogRepository incidentLogRepository, LogIndexingService logIndexingService) {
        this.incidentRepository = incidentRepository;
        this.incidentLogRepository = incidentLogRepository;
        this.logIndexingService = logIndexingService;
    }

    public LogUploadResponse uploadLog(Long incidentId, MultipartFile file){
        Incident incident = incidentRepository.findById(incidentId).orElseThrow(()->new IncidentNotFoundException());
        if(file.isEmpty()){
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        String filename = file.getOriginalFilename();

        if(filename == null || (!filename.endsWith(".log") && !filename.endsWith(".txt"))){
            throw new IllegalArgumentException("Only .log and .txt files are supported.");
        }

        String content;

        try{
            content = new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read uploaded log file.", ex);
        }

        IncidentLog log = new IncidentLog();
        log.setIncident(incident);
        log.setFilename(filename);
        log.setContent(content);

        IncidentLog savedLog = incidentLogRepository.save(log);
        logIndexingService.indexLog(savedLog);
        return new LogUploadResponse(savedLog.getId(), savedLog.getFilename());
    }
}
