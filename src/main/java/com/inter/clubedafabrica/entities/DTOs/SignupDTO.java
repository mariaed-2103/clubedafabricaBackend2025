package com.inter.clubedafabrica.entities.DTOs;

public record SignupDTO(
    String name,
    String email,
    String phone,
    String cpf,
    String password,
    String confirmPassword,
    String userType, // "user" ou "admin"
    String adminCode // opcional
) {

}
