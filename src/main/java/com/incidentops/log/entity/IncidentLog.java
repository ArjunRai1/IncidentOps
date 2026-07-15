package com.incidentops.log.entity;

import com.incidentops.incident.entity.Incident;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "incident_logs")
public class IncidentLog {
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    Incident incident;

    String filename;

    String content;

    LocalDateTime uploadedAt;
}
