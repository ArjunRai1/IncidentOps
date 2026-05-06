package com.incidentops.auth.dto;

public record AuthTokenResponse(
        String token,
        String tokenType,
        long expiresInSeconds
) {
}
