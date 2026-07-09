package com.incidentops.common.exception;

import com.incidentops.auth.exception.EmailAlreadyExistsException;
import com.incidentops.auth.exception.InvalidOtpException;
import com.incidentops.auth.exception.RegistrationExpiredException;
import com.incidentops.auth.exception.UsernameAlreadyExistsException;
import com.incidentops.incident.exception.IncidentNotFoundException;
import com.incidentops.incident.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
   @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException ex, HttpServletRequest request){
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI()
                );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
   }

   @ExceptionHandler(UsernameAlreadyExistsException.class)
   public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, HttpServletRequest request){
       ErrorResponse response = new ErrorResponse(
               LocalDateTime.now(),
               HttpStatus.CONFLICT.value(),
               ex.getMessage(),
               request.getRequestURI()
       );
       return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
   }

   @ExceptionHandler(RegistrationExpiredException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationExpired(RegistrationExpiredException ex, HttpServletRequest request){
       ErrorResponse response = new ErrorResponse(
               LocalDateTime.now(),
               HttpStatus.BAD_REQUEST.value(),
               ex.getMessage(),
               request.getRequestURI()
       );
       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
   }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOtp(InvalidOtpException ex, HttpServletRequest request){
        ErrorResponse response =    new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.GONE.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.GONE).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, HttpServletRequest request){
       ErrorResponse response = new ErrorResponse(
               LocalDateTime.now(),
               HttpStatus.NOT_FOUND.value(),
               ex.getMessage(),
               request.getRequestURI()
       );
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleIncidentNotFound(IncidentNotFoundException ex, HttpServletRequest request){
       ErrorResponse response = new ErrorResponse(
               LocalDateTime.now(),
               HttpStatus.NOT_FOUND.value(),
               ex.getMessage(),
               request.getRequestURI()
       );
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
