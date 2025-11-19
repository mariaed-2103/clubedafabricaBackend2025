package com.inter.clubedafabrica.entities.DTOs;

import lombok.Data;

@Data
public record SignupDTO(
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private String password;
    private String confirmPassword;
    private String userType; // "user" ou "admin"
    private String adminCode; // opcional
) {

}
