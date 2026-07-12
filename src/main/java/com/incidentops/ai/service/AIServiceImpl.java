package com.incidentops.ai.service;

import com.incidentops.ai.config.AIProperties;
import com.incidentops.ai.dto.ChatRequest;
import com.incidentops.ai.dto.ChatResponse;
import com.incidentops.ai.dto.SimilarIncidentResponse;
import com.incidentops.ai.exception.AIException;
import com.incidentops.ai.indexing.IncidentDocumentBuilder;
import com.incidentops.ai.prompt.PromptBuilder;
import com.incidentops.ai.retrieval.RetrievalService;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.exception.IncidentNotFoundException;
import com.incidentops.incident.repository.IncidentRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
public class AIServiceImpl implements AIService{
    private final AIProperties properties;
    private final ChatModel chatModel;
    private final RetrievalService retrievalService;
    private final PromptBuilder promptBuilder;
    private final IncidentRepository incidentRepository;
    private final IncidentDocumentBuilder incidentDocumentBuilder;
    public AIServiceImpl(AIProperties properties, @Qualifier("openAiChatModel") ChatModel chatModel, RetrievalService retrievalService, PromptBuilder promptBuilder, IncidentRepository incidentRepository, IncidentDocumentBuilder incidentDocumentBuilder) {
        this.properties = properties;
        this.chatModel = chatModel;
        this.retrievalService = retrievalService;
        this.promptBuilder = promptBuilder;
        this.incidentRepository = incidentRepository;
        this.incidentDocumentBuilder = incidentDocumentBuilder;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        ChatResponse response = new ChatResponse();
        //Previously, making Prompt directly from user query, now making it after retrieving and appending docs
        try {
            List<Document> documents = retrievalService.retrieve(request.getPrompt());
            String promptText = promptBuilder.build(request.getPrompt(), documents);
            Prompt prompt = new Prompt(new UserMessage(promptText));
            org.springframework.ai.chat.model.ChatResponse responseFromModel = chatModel.call(prompt);
            response.setAnswer(responseFromModel.getResult().getOutput().getText());
        } catch(Exception ex) {
            throw new AIException();
        }
        response.setModel(properties.getModel());
        response.setGeneratedAt(LocalDateTime.now());
        return response;
    }

    public List<SimilarIncidentResponse> getSimilarIncidents(Long incidentId){
        Incident incident = incidentRepository.findById(incidentId).orElseThrow(IncidentNotFoundException::new);
        String query = incident.getTitle() + " " + incident.getDescription();
        List<Document> documents = retrievalService.retrieve(query);
        Set<Long> similarIncidentIds = new LinkedHashSet<>();
        for(Document document : documents){
            Object metadata = document.getMetadata().get("incidentId");
            if (metadata == null) {
                continue;
            }
            Long similarIncidentId = Long.valueOf(metadata.toString());
            if(!similarIncidentId.equals(incidentId)){
                similarIncidentIds.add(similarIncidentId);
            }
        }
        List<Incident> similarIncidents = incidentRepository.findAllById(similarIncidentIds);
        List<SimilarIncidentResponse> response = new ArrayList<>();
        for(Incident similarIncident : similarIncidents){
            SimilarIncidentResponse similarIncidentResponse = new SimilarIncidentResponse();
            similarIncidentResponse.setId(similarIncident.getId());
            similarIncidentResponse.setTitle(similarIncident.getTitle());
            similarIncidentResponse.setStatus(similarIncident.getStatus());
            similarIncidentResponse.setPriority(similarIncident.getPriority());
            response.add(similarIncidentResponse);
        }
        return response;
    }
}
