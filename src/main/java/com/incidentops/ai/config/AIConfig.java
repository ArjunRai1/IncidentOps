package com.incidentops.ai.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(AIProperties.class)
public class AIConfig {
    @Bean
    @Primary
    public EmbeddingModel embeddingModel(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        return embeddingModel;
    }

}
