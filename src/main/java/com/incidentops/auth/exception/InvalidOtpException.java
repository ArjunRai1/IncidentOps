package com.incidentops.auth.exception;

public class InvalidOtpException extends RuntimeException{
    public InvalidOtpException(){
        super("Invalid OTP");
    }
}
