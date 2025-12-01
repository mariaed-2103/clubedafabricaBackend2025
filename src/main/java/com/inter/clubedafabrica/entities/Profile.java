package com.inter.clubedafabrica.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 20)
    private String phone; // pode ser nulo

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl; // opcional

    @Column(nullable = false)
    private String status = "inactive";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // pode ser nulo

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    private String userType; // user / admin
}
