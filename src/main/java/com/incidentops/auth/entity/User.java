package com.incidentops.auth.entity;

import com.incidentops.auth.redis.PendingRegistration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name="createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();;
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        LocalDateTime now = LocalDateTime.now();;
    }

    public static User from(PendingRegistration registration){
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setPassword(registration.getHashedPassword());
        return user;
    }
}
