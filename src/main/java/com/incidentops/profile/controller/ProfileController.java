package com.incidentops.profile.controller;

import com.incidentops.profile.dto.*;
import com.incidentops.profile.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> getProfile() {
        return ResponseEntity.ok(profileService.getProfile());
    }

    @PutMapping
    public ResponseEntity<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest request){
        profileService.updateProfile(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/request")
    public ResponseEntity<Void> requestPasswordChange(@Valid @RequestBody ChangePasswordRequest request) {
        profileService.requestPasswordChange(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/verify")
    public ResponseEntity<Void> verifyPasswordChange(@Valid @RequestBody VerifyPasswordChangeRequest request) {
        profileService.verifyPasswordChange(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/request")
    public ResponseEntity<Void> requestEmailChange(@Valid @RequestBody ChangeEmailRequest request) {
        profileService.requestEmailChange(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/verify")
    public ResponseEntity<Void> verifyEmailChange(@Valid @RequestBody VerifyEmailChangeRequest request) {
        profileService.verifyEmailChange(request);
        return ResponseEntity.ok().build();
    }
}