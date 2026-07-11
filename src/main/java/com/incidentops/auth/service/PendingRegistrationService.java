package com.incidentops.auth.service;

import com.incidentops.auth.redis.PendingRegistration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class PendingRegistrationService {

    private static final Duration OTP_TTL = Duration.ofMinutes(5);
    private final RedisTemplate<String, PendingRegistration> redisTemplate;

    public PendingRegistrationService(RedisTemplate<String, PendingRegistration> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(String prefix, String email) {
        return prefix + ":" + email;
    }

    public void save(PendingRegistration registration) {
        String key = getKey("registration", registration.getEmail());
        redisTemplate.opsForValue().set(key, registration, OTP_TTL);
    }

    public Optional<PendingRegistration> findByEmail(String email) {
        String key = getKey("registration", email);
        PendingRegistration registration = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(registration);
    }

    public void delete(String email) {
        String key = getKey("registration", email);
        redisTemplate.delete(key);
    }

    public void savePasswordReset(PendingRegistration registration) {
        String key = getKey("password-reset", registration.getEmail());
        redisTemplate.opsForValue().set(key, registration, OTP_TTL);
    }

    public Optional<PendingRegistration> findPasswordResetByEmail(String email) {
        String key = getKey("password-reset", email);
        PendingRegistration registration = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(registration);
    }

    public void deletePasswordReset(String email) {
        String key = getKey("password-reset", email);
        redisTemplate.delete(key);
    }
}