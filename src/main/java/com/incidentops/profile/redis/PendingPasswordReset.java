package com.incidentops.profile.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingPasswordReset {
    private String email;

    private String hashedPassword;

    private String otp;
}
