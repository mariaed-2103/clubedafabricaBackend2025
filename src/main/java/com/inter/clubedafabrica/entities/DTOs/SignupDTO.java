package com.inter.clubedafabrica.entities.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupDTO {

    @NotNull(message = "O nome é obrigatório.")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres.")
    private String name;

    @NotNull(message = "O email é obrigatório.")
    @Email(message = "Email inválido.")
    private String email;

    @NotNull(message = "O telefone é obrigatório.")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter 10 ou 11 dígitos.")
    private String phone; // opcional

    @NotNull(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    private String cpf;

    @NotNull(message = "A senha é obrigatória.")
    private String password;

    @NotNull(message = "A confirmação de senha é obrigatória.")
    private String confirmPassword;

    @NotNull(message = "O tipo de usuário é obrigatório.")
    private String userType;

    private String adminCode; // opcional
}
