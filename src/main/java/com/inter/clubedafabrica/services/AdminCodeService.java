package com.inter.clubedafabrica.services;

import com.inter.clubedafabrica.entities.AdminCode;
import com.inter.clubedafabrica.repositories.AdminCodeRepository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class AdminCodeService {

    @Autowired
    private AdminCodeRepository adminCodeRepository;

    public AdminCode generateNewCode() {
    String generatedCode = "ADMIN" + (int)(Math.random() * 9000 + 1000);

    AdminCode code = new AdminCode();
    code.setCode(generatedCode);
    code.setIsUsed(false);
    code.setCreatedAt(LocalDateTime.now());
    code.setCreatedBy(1L);

    return adminCodeRepository.save(code);
    }

    public boolean verifyAdminCode(String code) {
    return adminCodeRepository.findByCodeAndIsUsedFalse(code)
            .isPresent();
}


}

