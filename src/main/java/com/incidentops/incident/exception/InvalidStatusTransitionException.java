package com.incidentops.incident.exception;

public class InvalidStatusTransitionException extends RuntimeException{
    public InvalidStatusTransitionException(){
        super("Invalid state transition");
    }
}
