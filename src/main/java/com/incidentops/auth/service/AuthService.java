package com.incidentops.auth.service;

import com.incidentops.auth.dto.RegisterRequest;
import com.incidentops.auth.mail.MailService;
import com.incidentops.auth.otp.OtpGenerator;
import com.incidentops.auth.redis.PendingRegistration;
import com.incidentops.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final OtpGenerator otpGenerator;

    private final PendingRegistrationService pendingRegistrationService;

    private final MailService mailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, OtpGenerator otpGenerator, PendingRegistrationService pendingRegistrationService, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpGenerator = otpGenerator;
        this.pendingRegistrationService = pendingRegistrationService;
        this.mailService = mailService;
    }

    public void register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }
        if(userRepository.existsByUsername(request.getUsername())){
            throw new IllegalArgumentException("Username already exists");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        String otp = otpGenerator.generateOtp();

        PendingRegistration registration = new PendingRegistration(request.getUsername(), request.getEmail(), hashedPassword, otp);
        pendingRegistrationService.save(registration);

        mailService.sendOtp(registration.getEmail(), registration.getOtp());
    }
}
