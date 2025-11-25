package com.inter.clubedafabrica.entities.DTOs;

public record LoginResponse(
        Long id,
        String name,
        String email,
        String phone,
        String cpf,
        String status,
        String userType
) {}
