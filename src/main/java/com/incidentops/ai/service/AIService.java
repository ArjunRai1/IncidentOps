package com.incidentops.ai.service;

import com.incidentops.ai.dto.ChatRequest;
import com.incidentops.ai.dto.ChatResponse;

public interface AIService {
    ChatResponse chat(ChatRequest request);
}

