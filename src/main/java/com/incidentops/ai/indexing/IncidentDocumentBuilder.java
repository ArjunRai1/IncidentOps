package com.incidentops.ai.indexing;

import com.incidentops.comment.entity.Comment;
import com.incidentops.comment.repository.CommentRepository;
import com.incidentops.incident.entity.Incident;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import org.springframework.ai.document.Document;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class IncidentDocumentBuilder {
    private final CommentRepository commentRepository;

    public IncidentDocumentBuilder(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Document build(Incident incident) {
        List<Comment> comments = commentRepository.findByIncidentId(incident.getId(), Sort.by("createdAt").ascending());
        String commentsText = comments.stream()
                .map(Comment::getComment)
                .collect(Collectors.joining("\n- ", "- ", ""));
        String content = """
        Title: %s

        Description: %s

        Status: %s

        Priority: %s
        
        Comments: %s

        Created At: %s

        Updated At: %s
        """.formatted(
                incident.getTitle(),
                incident.getDescription(),
                incident.getStatus(),
                incident.getPriority(),
                commentsText,
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
