package com.inter.clubedafabrica.entities.DTOs;

import lombok.Data;

@Data
public record LoginDTO(
     private String email;
     private String password;
) {
}
