package com.inter.clubedafabrica.services;

import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.entities.DTOs.LoginDTO;
import com.inter.clubedafabrica.entities.DTOs.SignupDTO;
import com.inter.clubedafabrica.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //   GET STATUS

    public String getStatus(Long id) {
        return repo.findById(id)
                .map(Profile::getStatus)
                .orElse("NOT_FOUND");
    }

    //   VERIFY ADMIN CODE

    public boolean isAdmin(Profile p) {
        return p.getUserType() != null && p.getUserType().equals("admin");
    }
}
