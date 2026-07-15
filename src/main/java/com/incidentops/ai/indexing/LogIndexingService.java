package com.incidentops.ai.indexing;

import com.incidentops.log.entity.IncidentLog;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogIndexingService {

    private final LogDocumentBuilder logDocumentBuilder;
    private final TextSplitter textSplitter;
    private final VectorStore vectorStore;

    public LogIndexingService(LogDocumentBuilder logDocumentBuilder, TextSplitter textSplitter, VectorStore vectorStore) {
        this.logDocumentBuilder = logDocumentBuilder;
        this.textSplitter = textSplitter;
        this.vectorStore = vectorStore;
    }

    public void indexLog(IncidentLog log){
        Document document = logDocumentBuilder.build(log);
        List<Document> chunks = textSplitter.apply(List.of(document));
        vectorStore.add(chunks);
    }
}