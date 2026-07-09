package com.incidentops.incident.dto;

import com.incidentops.incident.entity.IncidentPriority;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateIncidentRequest {
    @Size(min=1, max=200)
    private String title;

    private String description;

    private Long assignedTo;

    private IncidentPriority priority;
}
