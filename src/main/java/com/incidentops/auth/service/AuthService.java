package com.incidentops.auth.service;

import com.incidentops.auth.dto.*;
import com.incidentops.auth.entity.Role;
import com.incidentops.auth.entity.User;
import com.incidentops.auth.exception.EmailAlreadyExistsException;
import com.incidentops.auth.exception.InvalidOtpException;
import com.incidentops.auth.exception.RegistrationExpiredException;
import com.incidentops.auth.exception.UsernameAlreadyExistsException;
import com.incidentops.auth.mail.MailService;
import com.incidentops.auth.otp.OtpGenerator;
import com.incidentops.auth.redis.PendingRegistration;
import com.incidentops.auth.repository.UserRepository;
import com.incidentops.incident.exception.UserNotFoundException;
import com.incidentops.profile.dto.UserProfileResponse;
import com.incidentops.security.JwtService;
import com.incidentops.security.UserPrincipal;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final OtpGenerator otpGenerator;

    private final PendingRegistrationService pendingRegistrationService;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, OtpGenerator otpGenerator, PendingRegistrationService pendingRegistrationService, MailService mailService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpGenerator = otpGenerator;
        this.pendingRegistrationService = pendingRegistrationService;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException();
        }
        if(userRepository.existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExistsException();
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        String otp = otpGenerator.generateOtp();

        String role = String.valueOf(Role.USER);

        PendingRegistration registration = new PendingRegistration(request.getUsername(), request.getEmail(), hashedPassword, otp, role);
        pendingRegistrationService.save(registration);

        mailService.sendOtp(registration.getEmail(), registration.getOtp());

    }
    public void verifyOtp(VerifyOTPRequest request){
        Optional<PendingRegistration> pendingRegistration = pendingRegistrationService.findByEmail(request.getEmail());
        PendingRegistration registration = pendingRegistration.orElseThrow(
                () -> new RegistrationExpiredException()
        );

        //Here, registration refers to the cache and request is the main request -  comparing them
        if (!registration.getOtp().equals(request.getOtp())) {
            throw new InvalidOtpException();
        }

        //Create a new user entity(from method defined in entity class itself) from cache and store it
        User user = User.from(registration);
        userRepository.save(user);

        //Delete redis entry immediately
        pendingRegistrationService.delete(request.getEmail());
        System.out.println("User created successfully");
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                request.getEmail(), request.getPassword()));
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // JWT generation comes next
        String token = jwtService.generateToken(principal);
        return new LoginResponse(token);
    }

    public UserProfileResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new UserNotFoundException());

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    public void forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new UserNotFoundException());
        String otp = otpGenerator.generateOtp();
        PendingRegistration pending = new PendingRegistration(null, user.getEmail(), null, otp, null);
        pendingRegistrationService.savePasswordReset(pending);
        mailService.sendOtp(user.getEmail(), otp);
    }

    public void resetPassword(ResetPasswordRequest request) {

        PendingRegistration pending = pendingRegistrationService.findPasswordResetByEmail(request.getEmail()).orElseThrow(()-> new RegistrationExpiredException());
        if (!pending.getOtp().equals(request.getOtp())) {
            throw new InvalidOtpException();
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new UserNotFoundException());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        pendingRegistrationService.deletePasswordReset(request.getEmail());
    }
}
