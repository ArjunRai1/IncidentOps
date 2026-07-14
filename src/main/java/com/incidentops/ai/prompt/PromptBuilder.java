package com.incidentops.ai.prompt;

import com.incidentops.ai.dto.SimilarIncidentResponse;
import com.incidentops.incident.entity.Incident;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
public class PromptBuilder {
    public String build(String question, List<Document> documents){
            String context = documents.stream().map(Document::getText).collect(Collectors.joining("\n\n---\n\n"));
            return """
            You are an AI assistant for IncidentOps.
            Answer the user's question using ONLY the retrieved incident documents below.
            If the answer cannot be found, respond exactly:
            "I couldn't find enough information in the indexed incidents."
            Retrieved Incident Documents:
            %s
            User Question:
            %s
            Answer:
            """.formatted(context, question);
    }

    public String buildSummaryPrompt(Document currentIncidentDocument, List<Document> documents){
        String context = documents.stream().map(Document::getText).collect(Collectors.joining("\n\n---\n\n"));
        return """
        You are an AI summary assistant for IncidentOps.
        Build an accurate summary using ONLY the information below.
        Current Incident:
        %s

        Similar Incident Context:
        %s

        Generate a concise technical summary.

        Include:
        - what happened
        - affected component
        - current investigation (if any)
        - probable impact
        Maximum 120 words.
        Do not invent facts.
        If similar incidents contain conflicting information, prioritize the Current Incident.
        Summary:
        """.formatted(currentIncidentDocument.getText(), context);
    }
}
