package com.incidentops.ai.controller;

import com.incidentops.ai.dto.ChatRequest;
import com.incidentops.ai.dto.ChatResponse;
import com.incidentops.ai.service.AIService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    private final AIService aiService;
    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request){
        ChatResponse response = aiService.chat(request);
        return ResponseEntity.ok(response);
    }
}
