package com.inter.clubedafabrica.services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCodeService {

    private final AdminCodeRepository adminRepo;

    public boolean verifyAdminCode(String code) {
        return adminRepo.existsByCodeAndIsUsedFalse(code);
    }
}