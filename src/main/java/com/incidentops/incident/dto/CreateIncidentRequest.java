package com.incidentops.incident.dto;

import com.incidentops.auth.entity.User;
import com.incidentops.incident.entity.IncidentPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateIncidentRequest {
    @NotBlank
    @Size(min=1, max=200)
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private IncidentPriority priority;

    private Long assignedTo;
}
