package com.incidentops.ai.indexing;

import com.incidentops.incident.entity.Incident;
import org.springframework.stereotype.Component;

import org.springframework.ai.document.Document;
import java.util.HashMap;
import java.util.Map;

@Component
public class IncidentDocumentBuilder {
    private final Incident incident;

    public IncidentDocumentBuilder(Incident incident) {
        this.incident = incident;
    }

    public Document build(Incident incident) {
        String content = """
        Title: %s

        Description: %s

        Status: %s

        Priority: %s

        Created At: %s

        Updated At: %s
        """.formatted(
                incident.getTitle(),
                incident.getDescription(),
                incident.getStatus(),
                incident.getPriority(),
                incident.getCreatedAt(),
                incident.getUpdatedAt()
        );

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("incidentId", incident.getId());
        metadata.put("status", incident.getStatus().name());
        metadata.put("priority", incident.getPriority().name());
        metadata.put("createdAt", incident.getCreatedAt().toString());
        metadata.put("updatedAt", incident.getUpdatedAt().toString());
        if (incident.getAssignedTo() != null) {
            metadata.put("assignedTo", incident.getAssignedTo().getId());
        }
        if (incident.getCreatedBy() != null) {
            metadata.put("createdBy", incident.getCreatedBy().getId());
        }
        return new Document(content, metadata);
    }
}
