package com.incidentops.comment.exception;

public class IncidentDoesNotExistException extends RuntimeException{
    public IncidentDoesNotExistException(){
        super("Incident does not exist");
    }
}
