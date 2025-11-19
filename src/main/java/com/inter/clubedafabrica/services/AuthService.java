package com.inter.clubedafabrica.services;

import com.seuprojeto.model.Profile;
import com.seuprojeto.repository.ProfileRepository;
import com.seuprojeto.repository.AdminCodeRepository;
import com.seuprojeto.dto.LoginDTO;
import com.seuprojeto.dto.SignupDTO;
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
        Optional<Profile> userOpt = profileRepository.findByEmail(loginDTO.getEmail());
        if(userOpt.isPresent()) {
            Profile user = userOpt.get();
            if(passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
                if("inactive".equals(user.getStatus())) return Optional.empty();
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public Profile signup(SignupDTO dto) throws Exception {
        if(!dto.getPassword().equals(dto.getConfirmPassword()))
            throw new Exception("Senhas não coincidem");

        if("admin".equals(dto.getUserType())) {
            if(dto.getAdminCode() == null || !adminCodeRepository.existsByCodeAndIsUsedFalse(dto.getAdminCode()))
                throw new Exception("Código de administrador inválido");
        }

        Profile user = new Profile();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setCpf(dto.getCpf());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setStatus("inactive"); // ativação posterior pelo admin
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Profile savedUser = profileRepository.save(user);

        // Se admin, marca código como usado
        if("admin".equals(dto.getUserType()) && dto.getAdminCode() != null) {
            adminCodeRepository.markAsUsed(dto.getAdminCode(), savedUser.getId());
        }

        return savedUser;
    }

}
