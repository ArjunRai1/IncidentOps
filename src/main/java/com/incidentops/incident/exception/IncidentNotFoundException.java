package com.incidentops.incident.exception;

public class IncidentNotFoundException extends RuntimeException {
    public IncidentNotFoundException(){
        super("Incident not found");
    }
}
