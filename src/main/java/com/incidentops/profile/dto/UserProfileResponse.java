package com.incidentops.profile.dto;

import com.incidentops.auth.entity.Role;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
    private Long id;

    private String username;

    private String email;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
