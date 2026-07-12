package com.incidentops.ai.controller;

import com.incidentops.ai.dto.ChatRequest;
import com.incidentops.ai.dto.ChatResponse;
import com.incidentops.ai.dto.SimilarIncidentResponse;
import com.incidentops.ai.indexing.IncidentDocumentBuilder;
import com.incidentops.ai.retrieval.RetrievalService;
import com.incidentops.ai.service.AIService;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.exception.IncidentNotFoundException;
import com.incidentops.incident.repository.IncidentRepository;
import jakarta.validation.Valid;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final AIService aiService;
    private final RetrievalService retrievalService;

    public AIController(AIService aiService, RetrievalService retrievalService) {
        this.aiService = aiService;
        this.retrievalService = retrievalService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request){
        ChatResponse response = aiService.chat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public List<String> search(@RequestParam String query) {
        return retrievalService.retrieve(query)
                .stream()
                .map(Document::getText)
                .toList();
    }

    @GetMapping("/incidents/{incidentId}/similar")
    public ResponseEntity<List<SimilarIncidentResponse>> getSimilarIncidents(@PathVariable Long incidentId) {
        return ResponseEntity.ok(aiService.getSimilarIncidents(incidentId));
    }
}
