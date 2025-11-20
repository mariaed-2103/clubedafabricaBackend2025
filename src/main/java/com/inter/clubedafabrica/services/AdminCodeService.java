package com.inter.clubedafabrica.services;

import com.inter.clubedafabrica.repositories.AdminCodeRepository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCodeService {

    private final AdminCodeRepository adminRepo;

    public boolean verifyAdminCode(String code) {
        return adminRepo.existsByCodeAndIsUsedFalse(code);
    }

    public void markAsUsed(String code, Long userId) {
        adminRepo.findByCode(code).ifPresent(admin -> {
        admin.setIsUsed(true);
        admin.setUsedBy(userId);
        admin.setUsedAt(LocalDateTime.now());
        adminRepo.save(admin);
    });
    }
}
