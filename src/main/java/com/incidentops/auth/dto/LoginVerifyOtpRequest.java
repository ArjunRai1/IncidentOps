package com.incidentops.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginVerifyOtpRequest(
        @NotBlank @Size(min = 3, max = 80) String username,
        @NotBlank @Pattern(regexp = "^\\d{6}$", message = "OTP must be a 6-digit number") String otp
) {
}
