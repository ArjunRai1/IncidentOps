package com.incidentops.ai.retrieval;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RetrievalService {
    private final VectorStore vectorStore;

    public RetrievalService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> retrieve(String userQuestion) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                        .query(userQuestion)
                        .topK(5)
                        .build()
        );
        return documents;
    }
}
