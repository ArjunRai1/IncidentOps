package com.incidentops.ai.indexing;

import com.incidentops.log.entity.IncidentLog;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LogDocumentBuilder {
    public Document build(IncidentLog log){
        String content = """
                Filename: %s
                Log:
                %s
                """.formatted(log.getFilename(), log.getContent());
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("incidentId", log.getIncident().getId());
        metadata.put("type", "LOG");
        metadata.put("filename", log.getFilename());
        return new Document(content, metadata);
    }
}