package com.inter.clubedafabrica.entities.DTOs;

import lombok.Data;

@Data
public class AdminCodeDTO {
    private String code;
    private String status;
    private String createdAt;
    private String usedAt;
}

