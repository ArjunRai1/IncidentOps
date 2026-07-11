package com.incidentops.ai.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatResponse {
        private String answer;
        private String model;
        private LocalDateTime generatedAt;
}
