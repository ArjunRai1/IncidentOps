package com.incidentops.auth.service;

import com.incidentops.auth.domain.OtpChallenge;
import com.incidentops.auth.domain.OtpFlowType;
import com.incidentops.auth.dto.AuthMessageResponse;
import com.incidentops.auth.dto.AuthTokenResponse;
import com.incidentops.auth.dto.LoginRequestOtpRequest;
import com.incidentops.auth.dto.LoginVerifyOtpRequest;
import com.incidentops.auth.dto.RegisterRequestOtpRequest;
import com.incidentops.auth.dto.RegisterVerifyOtpRequest;
import com.incidentops.common.exception.BadRequestException;
import com.incidentops.common.exception.UnauthorizedException;
import com.incidentops.common.security.JwtService;
import com.incidentops.users.domain.User;
import com.incidentops.users.domain.UserRole;
import com.incidentops.users.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final OtpChallengeRepository otpChallengeRepository;
    private final OtpEmailService otpEmailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${app.auth.otp-expiration-seconds:120}")
    private long otpExpirationSeconds;

    @Override
    @Transactional
    public AuthMessageResponse requestRegistrationOtp(RegisterRequestOtpRequest request) {
        String normalizedUsername = normalizeUsername(request.username());
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByUsername(normalizedUsername)) {
            throw new BadRequestException("Username is already in use");
        }
        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email is already in use");
        }

        otpChallengeRepository.deleteByFlowTypeAndEmail(OtpFlowType.REGISTER, normalizedEmail);
        otpChallengeRepository.deleteByFlowTypeAndUsername(OtpFlowType.REGISTER, normalizedUsername);

        String otp = generateOtp();
        OtpChallenge challenge = OtpChallenge.builder()
                .flowType(OtpFlowType.REGISTER)
                .email(normalizedEmail)
                .username(normalizedUsername)
                .fullName(request.fullName().trim())
                .passwordHash(passwordEncoder.encode(request.password()))
                .otpHash(passwordEncoder.encode(otp))
                .expiresAt(LocalDateTime.now().plusSeconds(otpExpirationSeconds))
                .build();

        otpChallengeRepository.save(challenge);
        otpEmailService.sendOtp(normalizedEmail, otp, "Registration");
        return new AuthMessageResponse("OTP sent to email. It will expire in 2 minutes.");
    }

    @Override
    @Transactional
    public AuthMessageResponse verifyRegistrationOtp(RegisterVerifyOtpRequest request) {
        String normalizedEmail = normalizeEmail(request.email());
        OtpChallenge challenge = otpChallengeRepository
                .findTopByFlowTypeAndEmailAndConsumedAtIsNullOrderByCreatedAtDesc(OtpFlowType.REGISTER, normalizedEmail)
                .orElseThrow(() -> new BadRequestException("No active registration challenge found for this email"));

        validateOtp(challenge, request.otp());

        if (userRepository.existsByUsername(challenge.getUsername())) {
            throw new BadRequestException("Username is already in use");
        }
        if (userRepository.existsByEmail(challenge.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        User user = User.builder()
                .username(challenge.getUsername())
                .email(challenge.getEmail())
                .passwordHash(challenge.getPasswordHash())
                .fullName(challenge.getFullName())
                .enabled(true)
                .roles(Set.of(UserRole.ENGINEER))
                .build();
        userRepository.save(user);

        challenge.setConsumedAt(LocalDateTime.now());
        otpChallengeRepository.save(challenge);
        otpChallengeRepository.deleteByFlowTypeAndEmail(OtpFlowType.REGISTER, normalizedEmail);
        otpChallengeRepository.deleteByFlowTypeAndUsername(OtpFlowType.REGISTER, challenge.getUsername());

        return new AuthMessageResponse("Registration verified successfully. You can now log in.");
    }

    @Override
    @Transactional
    public AuthMessageResponse requestLoginOtp(LoginRequestOtpRequest request) {
        String normalizedUsername = normalizeUsername(request.username());
        User user = userRepository.findByUsername(normalizedUsername)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    normalizedUsername,
                    request.password()
            ));
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Invalid username or password");
        }

        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new UnauthorizedException("User account is disabled");
        }

        otpChallengeRepository.deleteByFlowTypeAndUsername(OtpFlowType.LOGIN, normalizedUsername);

        String otp = generateOtp();
        OtpChallenge challenge = OtpChallenge.builder()
                .flowType(OtpFlowType.LOGIN)
                .email(user.getEmail())
                .username(user.getUsername())
                .userId(user.getId())
                .otpHash(passwordEncoder.encode(otp))
                .expiresAt(LocalDateTime.now().plusSeconds(otpExpirationSeconds))
                .build();
        otpChallengeRepository.save(challenge);

        otpEmailService.sendOtp(user.getEmail(), otp, "Login");
        return new AuthMessageResponse("OTP sent to your email. It will expire in 2 minutes.");
    }

    @Override
    @Transactional
    public AuthTokenResponse verifyLoginOtp(LoginVerifyOtpRequest request) {
        String normalizedUsername = normalizeUsername(request.username());
        OtpChallenge challenge = otpChallengeRepository
                .findTopByFlowTypeAndUsernameAndConsumedAtIsNullOrderByCreatedAtDesc(OtpFlowType.LOGIN, normalizedUsername)
                .orElseThrow(() -> new UnauthorizedException("No active login challenge found for this user"));

        validateOtp(challenge, request.otp());

        User user = userRepository.findById(challenge.getUserId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new UnauthorizedException("User account is disabled");
        }

        challenge.setConsumedAt(LocalDateTime.now());
        otpChallengeRepository.save(challenge);
        otpChallengeRepository.deleteByFlowTypeAndUsername(OtpFlowType.LOGIN, normalizedUsername);

        String token = jwtService.generateToken(user);
        return new AuthTokenResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }

    private void validateOtp(OtpChallenge challenge, String otp) {
        if (challenge.getConsumedAt() != null) {
            throw new BadRequestException("OTP is already used");
        }
        if (challenge.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP has expired");
        }
        if (!passwordEncoder.matches(otp, challenge.getOtpHash())) {
            throw new BadRequestException("Invalid OTP");
        }
    }

    private String generateOtp() {
        int random = ThreadLocalRandom.current().nextInt(100000, 1000000);
        return String.valueOf(random);
    }

    private String normalizeUsername(String username) {
        return username.trim().toLowerCase();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}
