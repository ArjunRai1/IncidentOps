package com.incidentops.ai.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incidentops.ai.config.AIProperties;
import com.incidentops.ai.dto.*;
import com.incidentops.ai.exception.AIException;
import com.incidentops.ai.indexing.IncidentDocumentBuilder;
import com.incidentops.ai.prompt.PromptBuilder;
import com.incidentops.ai.retrieval.RetrievalService;
import com.incidentops.comment.entity.Comment;
import com.incidentops.comment.repository.CommentRepository;
import com.incidentops.incident.entity.Incident;
import com.incidentops.incident.exception.IncidentNotFoundException;
import com.incidentops.incident.repository.IncidentRepository;
import com.incidentops.log.entity.IncidentLog;
import com.incidentops.log.repository.IncidentLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class AIServiceImpl implements AIService{
    private final AIProperties properties;
    private final ChatModel chatModel;
    private final RetrievalService retrievalService;
    private final PromptBuilder promptBuilder;
    private final IncidentRepository incidentRepository;
    private final IncidentDocumentBuilder incidentDocumentBuilder;
    private final CommentRepository commentRepository;
    private final IncidentLogRepository incidentLogRepository;
    private final ObjectMapper objectMapper;
    public AIServiceImpl(AIProperties properties, @Qualifier("openAiChatModel") ChatModel chatModel, RetrievalService retrievalService, PromptBuilder promptBuilder, IncidentRepository incidentRepository, IncidentDocumentBuilder incidentDocumentBuilder, CommentRepository commentRepository, IncidentLogRepository incidentLogRepository, ObjectMapper objectMapper) {
        this.properties = properties;
        this.chatModel = chatModel;
        this.retrievalService = retrievalService;
        this.promptBuilder = promptBuilder;
        this.incidentRepository = incidentRepository;
        this.incidentDocumentBuilder = incidentDocumentBuilder;
        this.commentRepository = commentRepository;
        this.incidentLogRepository = incidentLogRepository;
        this.objectMapper = objectMapper;
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
            log.error("Error in sending ai chat response", ex);
        }
        response.setModel(properties.getModel());
        response.setGeneratedAt(LocalDateTime.now());
        return response;
    }

    //Clean json to help with model output for RCA
    private String cleanJson(String response) {
        return response.replace("```json", "").replace("```", "").trim();
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

    public SummaryResponse summarizeIncident(Long incidentId){
        Incident incident = incidentRepository.findById(incidentId).orElseThrow(IncidentNotFoundException::new);
        Document currentIncidentDocument = incidentDocumentBuilder.build(incident);
        String query = incident.getTitle() + " " + incident.getDescription();
        List<Document> documents = retrievalService.retrieve(query);
        String promptText = promptBuilder.buildSummaryPrompt(currentIncidentDocument, documents);
        Prompt prompt = new Prompt(new UserMessage(promptText));
        SummaryResponse summaryResponse = new SummaryResponse();
        org.springframework.ai.chat.model.ChatResponse responseFromModel = chatModel.call(prompt);
        summaryResponse.setSummary(responseFromModel.getResult().getOutput().getText());
        return summaryResponse;
    }

    public IncidentAnalysisResponse analyzeIncident(Long incidentId){
        Incident incident = incidentRepository.findById(incidentId).orElseThrow(IncidentNotFoundException::new);
        List<Comment> comments = commentRepository.findByIncidentId(incidentId, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<IncidentLog> logs = incidentLogRepository.findByIncidentId(incidentId);
        String query = incident.getTitle() + " " + incident.getDescription();
        List<Document> retrievedDocuments = retrievalService.retrieve(query);
        String promptText = promptBuilder.buildIncidentAnalysisPrompt(incident, comments, logs, retrievedDocuments);

        Prompt prompt = new Prompt(new UserMessage(promptText));

        try{
            org.springframework.ai.chat.model.ChatResponse responseFromModel = chatModel.call(prompt);
            String answer = responseFromModel.getResult().getOutput().getText();
            answer = cleanJson(answer);
            return objectMapper.readValue(answer, IncidentAnalysisResponse.class);
        } catch (Exception ex){
            log.error("Error in ai incident analysis (RCA)", ex);
            throw new AIException();
        }
    }
}
