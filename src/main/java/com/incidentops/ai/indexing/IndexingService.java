package com.incidentops.ai.indexing;
import com.incidentops.incident.entity.Incident;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IndexingService {
    private final IncidentDocumentBuilder incidentDocumentBuilder;
    private final ChunkingService chunkingService;
    private final VectorStore vectorStore;

    public IndexingService(IncidentDocumentBuilder incidentDocumentBuilder, ChunkingService chunkingService, VectorStore vectorStore) {
        this.incidentDocumentBuilder = incidentDocumentBuilder;
        this.chunkingService = chunkingService;
        this.vectorStore = vectorStore;
    }

    public void indexIncident(Incident incident){
        Document document = incidentDocumentBuilder.build(incident);
        List<Document> chunks = chunkingService.chunking(document);
        vectorStore.add(chunks);
    }
}
