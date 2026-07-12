package com.incidentops.ai.dto;

import com.incidentops.incident.entity.IncidentPriority;
import com.incidentops.incident.entity.IncidentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimilarIncidentResponse {
    private Long id;
    private String title;
    private IncidentPriority priority;
    private IncidentStatus status;
}
