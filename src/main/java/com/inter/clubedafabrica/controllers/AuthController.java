package com.inter.clubedafabrica.controllers;

import com.seuprojeto.service.AuthService;
import com.seuprojeto.dto.LoginDTO;
import com.seuprojeto.dto.SignupDTO;
import com.seuprojeto.model.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Profile login(@RequestBody LoginDTO loginDTO) throws Exception {
        Optional<Profile> user = authService.login(loginDTO);
        if(user.isEmpty()) throw new Exception("Email ou senha inválidos, ou conta inativa");
        return user.get();
    }

    @PostMapping("/signup")
    public Profile signup(@RequestBody SignupDTO dto) throws Exception {
        return authService.signup(dto);
    }

}
