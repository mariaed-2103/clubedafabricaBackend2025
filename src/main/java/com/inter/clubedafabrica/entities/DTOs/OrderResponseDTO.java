package com.inter.clubedafabrica.entities.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
    Long id,
    Long userId,
    String status,
    Double totalAmount,
    LocalDateTime createdAt,
    List<OrderItemResponseDTO> items
) {

}
