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

    private String getKey(String email) {
        return "registration:" + email;
    }

    public void save(PendingRegistration registration) {
        String key = getKey(registration.getEmail());
        redisTemplate.opsForValue().set(key, registration, OTP_TTL);
    }

    public Optional<PendingRegistration> findByEmail(String email) {
        String key = getKey(email);
        PendingRegistration registration = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(registration);
    }

    public void delete(String email) {
        String key = getKey(email);
        redisTemplate.delete(key);
    }
}