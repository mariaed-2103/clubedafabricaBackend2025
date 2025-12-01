package com.inter.clubedafabrica.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@Data
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(nullable = false, length = 30)
    private String role;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
