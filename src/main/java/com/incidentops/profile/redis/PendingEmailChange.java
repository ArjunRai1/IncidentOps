package com.incidentops.profile.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingEmailChange {
    private Long userId;
    private String newEmail;
    private String otp;
}