package com.incidentops.ai.service;

import com.incidentops.ai.dto.*;

import java.util.List;

public interface AIService {
    ChatResponse chat(ChatRequest request);
    List<SimilarIncidentResponse> getSimilarIncidents(Long incidentId);
    SummaryResponse summarizeIncident(Long incidentId);
    IncidentAnalysisResponse analyzeIncident(Long incidentId);
}

