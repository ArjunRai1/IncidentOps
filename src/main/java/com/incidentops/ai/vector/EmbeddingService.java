package com.incidentops.ai.vector;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public EmbeddingService(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] generateEmbedding(String text){
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text to embed cannot be blank");
        }
        EmbeddingResponse response = embeddingModel.call(new EmbeddingRequest(List.of(text), null));
        return response.getResults().getFirst().getOutput();
    }
}
