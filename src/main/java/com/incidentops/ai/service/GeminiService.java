package com.incidentops.ai.service;

import com.incidentops.ai.config.AIProperties;
import com.incidentops.ai.dto.ChatRequest;
import com.incidentops.ai.dto.ChatResponse;
import com.incidentops.ai.exception.AIException;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class GeminiService implements AIService{
    private final AIProperties properties;
    private final ChatModel chatModel;
    public GeminiService(AIProperties properties, ChatModel chatModel) {
        this.properties = properties;
        this.chatModel = chatModel;
    }

    @Override
    public ChatResponse chat(ChatRequest request) {
        ChatResponse response = new ChatResponse();
        UserMessage userMessage = new UserMessage(request.getPrompt());
        Prompt prompt = new Prompt(userMessage);
        try {
            org.springframework.ai.chat.model.ChatResponse responseFromModel = chatModel.call(prompt);
            String answer = responseFromModel.getResult().getOutput().getText();
            response.setAnswer(answer);

        } catch(Exception ex) {
            throw new AIException();
        }
        response.setModel(properties.getModel());
        response.setGeneratedAt(LocalDateTime.now());
        return response;
    }
}
