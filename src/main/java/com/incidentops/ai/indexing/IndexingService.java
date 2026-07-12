package com.incidentops.ai.indexing;

import com.incidentops.ai.vector.EmbeddingService;
import com.incidentops.incident.entity.Incident;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IndexingService {
    private final EmbeddingService embeddingService;
    //private final VectorDocumentRepository vectorDocumentRepository;

    public IndexingService(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }
    public void indexIncident(Incident incident){

    }
}
