package com.incidentops.auth.exception;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(){
        super("Email already exists");
    }
}
