package com.incidentops.incident.dto;

import com.incidentops.auth.entity.User;
import com.incidentops.incident.entity.IncidentPriority;
import com.incidentops.incident.entity.IncidentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IncidentResponse {
    private long id;
    private String title;

    private String description;

    private IncidentPriority priority;

    private IncidentStatus status;

    private Long createdById;
    private String createdBy;

    private Long assignedToId;
    private String assignedTo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
