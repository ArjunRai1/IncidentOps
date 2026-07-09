package com.incidentops.audit.dto;

import com.incidentops.audit.entity.Action;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditResponse {

    private Long id;

    private Action action;

    private String description;

    private String email;

    private LocalDateTime createdAt;
}
