package com.incidentops.profile.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPasswordChangeRequest {
    @NotBlank
    private String otp;
}
