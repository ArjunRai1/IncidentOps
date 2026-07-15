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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", nullable = false)
    private Incident incident;

    private String filename;

    private String content;

    private LocalDateTime uploadedAt;

    @PrePersist
    public void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        uploadedAt = now;
    }
}
