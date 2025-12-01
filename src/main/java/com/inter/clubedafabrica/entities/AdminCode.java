package com.inter.clubedafabrica.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "admin_codes")
@Data
public class AdminCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, length = 50)
    private String code;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @NotNull
    @Column(name = "is_used", nullable = false)
    private Boolean isUsed = false;

    @Column(name = "used_by")
    private Long usedBy; // pode ser nulo

    @NotNull
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt; // pode ser nulo
}
