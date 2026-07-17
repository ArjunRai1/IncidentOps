package com.incidentops.profile.service;

import com.incidentops.auth.entity.User;
import com.incidentops.auth.exception.InvalidOtpException;
import com.incidentops.auth.mail.MailService;
import com.incidentops.auth.redis.PendingRegistration;
import com.incidentops.auth.repository.UserRepository;
import com.incidentops.auth.service.PendingRegistrationService;
import com.incidentops.incident.exception.UserNotFoundException;
import com.incidentops.profile.dto.*;
import com.incidentops.profile.redis.PendingEmailChange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.incidentops.auth.otp.OtpGenerator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PendingRegistrationService pendingRegistrationService;
    private final MailService mailService;
    private final OtpGenerator otpGenerator;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder, PendingRegistrationService pendingRegistrationService, MailService mailService, OtpGenerator otpGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pendingRegistrationService = pendingRegistrationService;
        this.mailService = mailService;
        this.otpGenerator = otpGenerator;
    }

    private User getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());
    }

    public UserProfileResponse getProfile(){
        User user = getCurrentUser();
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        return response;
    }

    public void updateProfile(UpdateProfileRequest request){
        User user = getCurrentUser();
        if(!user.getUsername().equals(request.getUsername()) && userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("Username already exists");
        }
        user.setUsername(request.getUsername());
        userRepository.save(user);
        log.info("User profile updated");
    }

    public void requestPasswordChange(ChangePasswordRequest request){
        User user = getCurrentUser();
        if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
            throw new RuntimeException("New password cannot be same as current password");
        }
        String otp = otpGenerator.generateOtp();
        PendingRegistration pending = new PendingRegistration();
        pending.setUsername(user.getUsername());
        pending.setEmail(user.getEmail());
        pending.setHashedPassword(passwordEncoder.encode(request.getNewPassword()));
        pending.setOtp(otp);
        pendingRegistrationService.savePasswordReset(pending);
        mailService.sendOtp(user.getEmail(), otp);
        log.info("OTP sent to {} for password change", user.getEmail());
    }

    public void verifyPasswordChange(VerifyPasswordChangeRequest request){
        User user = getCurrentUser();
        PendingRegistration pending = pendingRegistrationService.findPasswordResetByEmail(user.getEmail()).orElseThrow(() -> new RuntimeException("OTP expired"));
        if(!pending.getOtp().equals(request.getOtp())){
            throw new InvalidOtpException();
        }
        user.setPassword(pending.getHashedPassword());
        userRepository.save(user);
        pendingRegistrationService.deletePasswordReset(user.getEmail());
        log.info("Password successfully changed");
    }

    public void requestEmailChange(ChangeEmailRequest request){
        User user = getCurrentUser();
        if (user.getEmail().equalsIgnoreCase(request.getNewEmail())) {
            throw new RuntimeException("New email must be different.");
        }
        if (userRepository.existsByEmail(request.getNewEmail())) {
            throw new RuntimeException("Email already exists.");
        }
        String otp = otpGenerator.generateOtp();

        PendingEmailChange pending = new PendingEmailChange(user.getId(), request.getNewEmail(), otp);
        pendingRegistrationService.saveEmailChange(pending);
        mailService.sendOtp(request.getNewEmail(), otp);
        log.info("OTP sent to {} for email update", request.getNewEmail());
    }

    public void verifyEmailChange(VerifyEmailChangeRequest request){
        User user = getCurrentUser();
        PendingEmailChange pending = pendingRegistrationService.findEmailChange(user.getId()).orElseThrow(RuntimeException::new);
        if (!pending.getOtp().equals(request.getOtp())) {
            throw new InvalidOtpException();
        }
        user.setEmail(pending.getNewEmail());
        userRepository.save(user);
        pendingRegistrationService.deleteEmailChange(user.getId());
        log.info("Email changed successfully");
    }
}