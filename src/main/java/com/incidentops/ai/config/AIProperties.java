package com.incidentops.ai.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "incidentops.ai")
public class AIProperties {
    private String model;
    private Double temperature;
    private Integer maxTokens;
}
