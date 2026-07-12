package com.incidentops.ai.service;

import com.incidentops.ai.config.AIProperties;
import com.incidentops.ai.dto.ChatRequest;
import com.incidentops.ai.dto.ChatResponse;
import com.incidentops.ai.exception.AIException;
import com.incidentops.ai.prompt.PromptBuilder;
import com.incidentops.ai.retrieval.RetrievalService;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AIServiceImpl implements AIService{
    private final AIProperties properties;
    private final ChatModel chatModel;
    private final RetrievalService retrievalService;
    private final PromptBuilder promptBuilder;
    public AIServiceImpl(AIProperties properties, @Qualifier("openAiChatModel") ChatModel chatModel, RetrievalService retrievalService, PromptBuilder promptBuilder) {
        this.properties = properties;
        this.chatModel = chatModel;
        this.retrievalService = retrievalService;
        this.promptBuilder = promptBuilder;
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
}
