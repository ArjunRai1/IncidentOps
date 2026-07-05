package com.incidentops.auth.service;

import com.incidentops.auth.dto.RegisterRequest;
import com.incidentops.auth.dto.VerifyOTPRequest;
import com.incidentops.auth.entity.User;
import com.incidentops.auth.exception.EmailAlreadyExistsException;
import com.incidentops.auth.exception.InvalidOtpException;
import com.incidentops.auth.exception.RegistrationExpiredException;
import com.incidentops.auth.exception.UsernameAlreadyExistsException;
import com.incidentops.auth.mail.MailService;
import com.incidentops.auth.otp.OtpGenerator;
import com.incidentops.auth.redis.PendingRegistration;
import com.incidentops.auth.repository.UserRepository;
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

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, OtpGenerator otpGenerator, PendingRegistrationService pendingRegistrationService, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpGenerator = otpGenerator;
        this.pendingRegistrationService = pendingRegistrationService;
        this.mailService = mailService;
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

        PendingRegistration registration = new PendingRegistration(request.getUsername(), request.getEmail(), hashedPassword, otp);
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


}
