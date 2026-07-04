package com.incidentops.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    @Size(min=3, max=50)
    private String username;

    @NotBlank
    @Size(min=8, max=64)
    private String password;

    @NotBlank
    private String email;
}
