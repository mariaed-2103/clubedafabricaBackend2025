package com.inter.clubedafabrica.services;

import com.inter.clubedafabrica.entities.DTOs.LoginDTO;
import com.inter.clubedafabrica.entities.DTOs.SignupDTO;
import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.repositories.AdminCodeRepository;
import com.inter.clubedafabrica.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private AdminCodeRepository adminCodeRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ======================
    // LOGIN
    // ======================
    public Optional<Profile> login(LoginDTO loginDTO) {
        Optional<Profile> userOpt = profileRepository.findByEmail(loginDTO.email());

        if (userOpt.isEmpty()) return Optional.empty();

        Profile user = userOpt.get();

        // verifica senha
        if (!passwordEncoder.matches(loginDTO.password(), user.getPasswordHash()))
            return Optional.empty();

        // Verifica se usuário está ativo
        if ("inactive".equals(user.getStatus()))
            return Optional.empty();

        return Optional.of(user);
    }


    // ======================
    // SIGNUP
    // ======================
    public Profile signup(SignupDTO dto) throws Exception {

        // Senhas
        if (!dto.password().equals(dto.confirmPassword()))
            throw new Exception("Senhas não coincidem.");

        // Email duplicado
        if (profileRepository.findByEmail(dto.email()).isPresent())
            throw new Exception("E-mail já cadastrado.");

        // Se for admin → validar código
        if ("admin".equals(dto.userType())) {
            if (dto.adminCode() == null || !adminCodeRepository.existsByCodeAndIsUsedFalse(dto.adminCode())) {
                throw new Exception("Código de administrador inválido.");
            }
        }

        Profile user = new Profile();
            user.setName(dto.name());
            user.setEmail(dto.email());
            user.setPhone(dto.phone());
            user.setCpf(dto.cpf());
            user.setPasswordHash(encoder.encode(dto.password()));
            user.setUserType(dto.userType());
            user.setStatus("inactive"); // ou "active", dependendo da regra
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            // se for admin, valida de novo no back
            if ("admin".equals(dto.userType())) {
                boolean isValid = adminCodeRepository.existsByCodeAndIsUsedFalse(dto.adminCode());
                if (!isValid) {
                    throw new RuntimeException("Código de administrador inválido ou já utilizado.");
                }
            }

            Profile savedUser = profileRepository.save(user);

            if ("admin".equals(dto.userType())) {
                adminCodeRepository.markAsUsed(dto.adminCode(), savedUser.getId());
            }

            return savedUser;
    }
}
