package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.DTOs.LoginDTO;
import com.inter.clubedafabrica.entities.DTOs.SignupDTO;
import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.services.AdminCodeService;
import com.inter.clubedafabrica.services.AuthService; // <-- Importa AuthService
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // private final UserService userService; // <-- Remove, não existe
    private final AuthService authService; // <-- Adiciona dependência do AuthService
    private final AdminCodeService adminCodeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO req) { // <-- Usa LoginDTO
        Optional<Profile> userOpt = authService.login(req); // <-- Chama o login do AuthService

        if (userOpt.isEmpty()) {
            // Verifica a razão exata para melhor feedback no frontend
            Optional<Profile> existingUser = authService.findByEmail(req.email());
            
            if (existingUser.isPresent() && "inactive".equals(existingUser.get().getStatus())) {
                // Se a conta existe, mas está inativa (AuthService não retornou)
                // Retorna 403 Forbidden para indicar status diferente de credencial inválida
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sua conta está inativa. Aguarde a ativação de um administrador.");
            }
            
            // Senha incorreta ou email não encontrado
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas. Verifique seu email e senha.");
        }

        Profile user = userOpt.get();
        
        // Retorna 200 OK com os dados do perfil logado
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDTO req) { // <-- Usa SignupDTO
        try {
            Profile newUser = authService.signup(req); // <-- Chama o signup do AuthService
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser); // <-- Retorna 201 Created
        } catch (Exception e) {
            // Captura erros de validação (ex: senha não coincide, código admin inválido)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify-admin")
    public ResponseEntity<Boolean> verifyAdmin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        boolean valid = adminCodeService.verifyAdminCode(code);
        return ResponseEntity.ok(valid);
    }
}