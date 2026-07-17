package com.incidentops.ai.indexing;
import com.incidentops.ai.repository.VectorStoreRepository;
import com.incidentops.incident.entity.Incident;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import java.util.List;
@Slf4j
@Service
public class IndexingService {
    private final IncidentDocumentBuilder incidentDocumentBuilder;
    private final ChunkingService chunkingService;
    private final VectorStore vectorStore;
    private final VectorStoreRepository vectorStoreRepository;

    public IndexingService(IncidentDocumentBuilder incidentDocumentBuilder, ChunkingService chunkingService, VectorStore vectorStore, VectorStoreRepository vectorStoreRepository) {
        this.incidentDocumentBuilder = incidentDocumentBuilder;
        this.chunkingService = chunkingService;
        this.vectorStore = vectorStore;
        this.vectorStoreRepository = vectorStoreRepository;
    }

    public void indexIncident(Incident incident){
        vectorStoreRepository.deleteByIncidentId(incident.getId());
        Document document = incidentDocumentBuilder.build(incident);
        List<Document> chunks = chunkingService.chunking(document);
        vectorStore.add(chunks);
        log.info("Indexing done");
    }
}
