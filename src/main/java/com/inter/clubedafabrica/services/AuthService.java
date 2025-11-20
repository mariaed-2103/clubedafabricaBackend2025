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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Optional<Profile> login(LoginDTO loginDTO) {
        Optional<Profile> userOpt = profileRepository.findByEmail(loginDTO.email());
        if(userOpt.isPresent()) {
            Profile user = userOpt.get();
            if(passwordEncoder.matches(loginDTO.password(), user.getPasswordHash())) {
                if("inactive".equals(user.getStatus())) return Optional.empty();
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
    
    public Optional<Profile> findByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    public Profile signup(SignupDTO dto) throws Exception {
        if(!dto.password().equals(dto.confirmPassword()))
            throw new Exception("Senhas não coincidem");

        if("admin".equals(dto.userType())) {
            if(dto.adminCode() == null || !adminCodeRepository.existsByCodeAndIsUsedFalse(dto.adminCode()))
                throw new Exception("Código de administrador inválido");
        }

        Profile user = new Profile();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());
        user.setCpf(dto.cpf());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));
        user.setStatus("inactive"); // ativação posterior pelo admin
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Profile savedUser = profileRepository.save(user);

        // Se admin, marca código como usado
        if("admin".equals(dto.userType()) && dto.adminCode() != null) {
            adminCodeRepository.markAsUsed(dto.adminCode(), savedUser.getId());
        }

        return savedUser;
    }

}
