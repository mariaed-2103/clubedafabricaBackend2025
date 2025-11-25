package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.DTOs.LoginDTO;
import com.inter.clubedafabrica.entities.DTOs.LoginResponse;
import com.inter.clubedafabrica.entities.DTOs.SignupDTO;
import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.services.AdminCodeService;
import com.inter.clubedafabrica.services.AuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // endereço do front-end
public class AuthController {

    private final AuthService authService;
    private final AdminCodeService adminCodeService;

    // ======================
    // SIGNUP
    // ======================
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDTO dto) {
        try {
            Profile p = authService.signup(dto);
            return ResponseEntity.ok(p);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ======================
    // LOGIN
    // ======================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        var userOpt = authService.login(dto);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Credenciais inválidas!");
        }

        Profile p = userOpt.get();

        LoginResponse res = new LoginResponse(
                p.getId(),
                p.getName(),
                p.getEmail(),
                p.getPhone(),
                p.getCpf(),
                p.getStatus(),
                p.getUserType()
        );

        return ResponseEntity.ok(res);
    }


    // ======================
    // VERIFY ADMIN CODE
    // ======================
    @PostMapping("/verify-admin")
    public ResponseEntity<Boolean> verifyAdmin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        boolean isValid = adminCodeService.verifyAdminCode(code);
        return ResponseEntity.ok(isValid);
    }
}
