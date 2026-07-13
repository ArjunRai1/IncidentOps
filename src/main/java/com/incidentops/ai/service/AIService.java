package com.incidentops.ai.service;

import com.incidentops.ai.dto.ChatRequest;
import com.incidentops.ai.dto.ChatResponse;
import com.incidentops.ai.dto.SimilarIncidentResponse;
import com.incidentops.ai.dto.SummaryResponse;

import java.util.List;

public interface AIService {
    ChatResponse chat(ChatRequest request);
    List<SimilarIncidentResponse> getSimilarIncidents(Long incidentId);
    SummaryResponse summarizeIncident(Long incidentId);
}

