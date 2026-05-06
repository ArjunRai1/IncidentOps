package com.incidentops.auth.controller;

import com.incidentops.auth.dto.AuthMessageResponse;
import com.incidentops.auth.dto.AuthTokenResponse;
import com.incidentops.auth.dto.LoginRequestOtpRequest;
import com.incidentops.auth.dto.LoginVerifyOtpRequest;
import com.incidentops.auth.dto.RegisterRequestOtpRequest;
import com.incidentops.auth.dto.RegisterVerifyOtpRequest;
import com.incidentops.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/request-otp")
    public ResponseEntity<AuthMessageResponse> requestRegistrationOtp(
            @Valid @RequestBody RegisterRequestOtpRequest request
    ) {
        return ResponseEntity.ok(authService.requestRegistrationOtp(request));
    }

    @PostMapping("/register/verify-otp")
    public ResponseEntity<AuthMessageResponse> verifyRegistrationOtp(
            @Valid @RequestBody RegisterVerifyOtpRequest request
    ) {
        return ResponseEntity.ok(authService.verifyRegistrationOtp(request));
    }

    @PostMapping("/login/request-otp")
    public ResponseEntity<AuthMessageResponse> requestLoginOtp(
            @Valid @RequestBody LoginRequestOtpRequest request
    ) {
        return ResponseEntity.ok(authService.requestLoginOtp(request));
    }

    @PostMapping("/login/verify-otp")
    public ResponseEntity<AuthTokenResponse> verifyLoginOtp(
            @Valid @RequestBody LoginVerifyOtpRequest request
    ) {
        return ResponseEntity.ok(authService.verifyLoginOtp(request));
    }
}
