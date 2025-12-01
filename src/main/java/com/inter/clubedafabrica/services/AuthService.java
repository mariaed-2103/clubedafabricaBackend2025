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

        // ⚠️ REMOVIDO bloqueio interno de "inactive"
        // LoginController irá tratar isso

        return Optional.of(user);
    }


    // ======================
    // SIGNUP
    // ======================
    public Profile signup(SignupDTO dto) throws Exception {

        // Validação de senha
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new Exception("Senhas não coincidem.");
        }

        // Email duplicado
        if (profileRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new Exception("E-mail já cadastrado.");
        }

        // CPF duplicado
        if (profileRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new Exception("CPF já cadastrado.");
        }

        // Se for admin → validar código
        if ("admin".equalsIgnoreCase(dto.getUserType())) {
            if (dto.getAdminCode() == null ||
                    !adminCodeRepository.existsByCodeAndIsUsedFalse(dto.getAdminCode())) {
                throw new Exception("Código de administrador inválido.");
            }
        }

        // Criar o usuário
        Profile user = new Profile();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setCpf(dto.getCpf());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setUserType(dto.getUserType().toLowerCase());

        // 🟢 TODOS   começam INATIVOS
        user.setStatus("inactive");

        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);

        Profile savedUser = profileRepository.save(user);

        // Marcar adminCode como usado
        if ("admin".equalsIgnoreCase(dto.getUserType())) {
            adminCodeRepository.markAsUsed(dto.getAdminCode(), savedUser.getId());
        }

        return savedUser;
    }

}

