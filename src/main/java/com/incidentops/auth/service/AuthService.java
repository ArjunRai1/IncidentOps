package com.incidentops.auth.service;

import com.incidentops.auth.dto.AuthMessageResponse;
import com.incidentops.auth.dto.AuthTokenResponse;
import com.incidentops.auth.dto.LoginRequestOtpRequest;
import com.incidentops.auth.dto.LoginVerifyOtpRequest;
import com.incidentops.auth.dto.RegisterRequestOtpRequest;
import com.incidentops.auth.dto.RegisterVerifyOtpRequest;

public interface AuthService {

    AuthMessageResponse requestRegistrationOtp(RegisterRequestOtpRequest request);

    AuthMessageResponse verifyRegistrationOtp(RegisterVerifyOtpRequest request);

    AuthMessageResponse requestLoginOtp(LoginRequestOtpRequest request);

    AuthTokenResponse verifyLoginOtp(LoginVerifyOtpRequest request);
}
