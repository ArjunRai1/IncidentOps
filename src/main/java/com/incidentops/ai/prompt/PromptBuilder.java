package com.incidentops.ai.prompt;

import com.incidentops.ai.dto.SimilarIncidentResponse;
import com.incidentops.comment.entity.Comment;
import com.incidentops.incident.entity.Incident;
import com.incidentops.log.entity.IncidentLog;
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

    public String buildIncidentAnalysisPrompt(Incident incident, List<Comment> comments, List<IncidentLog> logs, List<Document> retrievedDocuments){
        String commentContext = comments.isEmpty() ? "No comments available." : comments.stream().map(Comment::getComment).collect(Collectors.joining("\n"));
        String logContext = logs.isEmpty() ? "No logs uploaded." : logs.stream().map(IncidentLog::getContent).collect(Collectors.joining("\n\n---\n\n"));
        String retrievedContext = retrievedDocuments.isEmpty() ? "No similar incidents found." : retrievedDocuments.stream().map(Document::getText).collect(Collectors.joining("\n\n---\n\n"));
        return """
                You are an AI incident response assistant.
                
                Analyze the incident using ONLY the information provided.
                
                Current Incident
                ----------------
                Title: %s
                
                Description: %s
                
                Status: %s
                
                Priority: %s
                
                Comments
                --------
                %s
                
                Logs
                ----
                %s
                
                Retrieved Similar Incidents
                ---------------------------
                %s
                
                Tasks
                
                1. Identify the single most probable root cause.
               
                2. Assign one confidence level:
                CRITICAL
                HIGH
                MEDIUM
                LOW
                
                3. List the evidence supporting your conclusion.
                
                4. Recommend immediate mitigation actions.
                
                5. Recommend long-term preventive recommendations.
                
                Rules
                
                - Use ONLY the supplied information.
                - Never invent facts.
                - If logs are unavailable, rely on comments and incident details.
                - If comments are unavailable, rely on logs.
                - If both are unavailable, use retrieved incidents if relevant.
                - If evidence is insufficient, explicitly state that.
                - Try to prefix the evidence with its source (only if possible and available).
                
                Return ONLY valid JSON matching this schema.
                
                {
                  "rootCause": "...",
                  "confidence": "...",
                  "evidence": [
                    "..."
                  ],
                  "immediateActions": [
                    "..."
                  ],
                  "preventiveRecommendations": [
                    "..."
                  ]
                }
                Return ONLY valid JSON.
              
                Do NOT wrap the JSON inside markdown.
                
                Do NOT include ```json or ```.
                
                Do NOT write explanations before or after the JSON.
                
                The first character of your response must be '{' and the last character must be '}'.
                """.formatted(incident.getTitle(), incident.getDescription(), incident.getStatus(), incident.getPriority(), commentContext, logContext, retrievedContext);
    }
}
