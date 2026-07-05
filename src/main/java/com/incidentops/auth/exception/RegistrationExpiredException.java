package com.incidentops.auth.exception;

public class RegistrationExpiredException extends RuntimeException{
    public RegistrationExpiredException(){
        super("Registration expired or not found");
    }
}
