package com.inter.clubedafabrica.entities.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
        Long id,
        String status,
        Double totalAmount,
        LocalDateTime createdAt,
        CustomerDTO customer,
        String pickupDate,
        String pickupTime,
        List<OrderItemResponseDTO> items
) {}
