package com.incidentops.auth.controller;

import com.incidentops.auth.dto.RegisterRequest;
import com.incidentops.auth.dto.VerifyOTPRequest;
import com.incidentops.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        authService.register(request);
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@Valid @RequestBody VerifyOTPRequest request){
        authService.verifyOtp(request);
        return ResponseEntity.ok("OTP verified successfully");
    }
}
