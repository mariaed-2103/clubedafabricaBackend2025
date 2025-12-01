package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.AdminCode;
import com.inter.clubedafabrica.entities.DTOs.AdminCodeDTO;
import com.inter.clubedafabrica.repositories.AdminCodeRepository;
import com.inter.clubedafabrica.services.AdminCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-codes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminCodeController {

    private final AdminCodeRepository adminCodeRepository;
    private final AdminCodeService adminCodeService;

    // ============================
    // LISTAR TODOS
    // ============================
    @GetMapping
    public List<AdminCodeDTO> getAllCodes() {
        return adminCodeRepository.findAll().stream().map(code -> {
            AdminCodeDTO dto = new AdminCodeDTO();
            dto.setCode(code.getCode());
            dto.setStatus(code.getIsUsed() ? "Usado" : "Disponível");
            dto.setCreatedAt(code.getCreatedAt().toString());
            dto.setUsedAt(code.getUsedAt() != null ? code.getUsedAt().toString() : null);
            return dto;
        }).toList();
    }

    // ============================
    // GERAR NOVO CÓDIGO
    // ============================
    @PostMapping
    public AdminCodeDTO createCode() {

        AdminCode saved = adminCodeService.generateNewCode();

        AdminCodeDTO dto = new AdminCodeDTO();
        dto.setCode(saved.getCode());
        dto.setStatus("Disponível");
        dto.setCreatedAt(saved.getCreatedAt().toString());
        dto.setUsedAt(null);

        return dto;
    }
}
