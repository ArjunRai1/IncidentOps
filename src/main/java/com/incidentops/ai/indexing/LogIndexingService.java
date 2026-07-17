package com.incidentops.ai.indexing;

import com.incidentops.log.entity.IncidentLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogIndexingService {

    private final LogDocumentBuilder logDocumentBuilder;
    private final ChunkingService chunkingService;;
    private final VectorStore vectorStore;

    public LogIndexingService(LogDocumentBuilder logDocumentBuilder, ChunkingService chunkingService, VectorStore vectorStore) {
        this.logDocumentBuilder = logDocumentBuilder;
        this.chunkingService = chunkingService;
        this.vectorStore = vectorStore;
    }

    public void indexLog(IncidentLog log){
        Document document = logDocumentBuilder.build(log);
        List<Document> chunks = chunkingService.chunking(document);
        vectorStore.add(chunks);
    }
}