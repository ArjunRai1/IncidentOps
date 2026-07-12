package com.incidentops.ai.prompt;

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
}
