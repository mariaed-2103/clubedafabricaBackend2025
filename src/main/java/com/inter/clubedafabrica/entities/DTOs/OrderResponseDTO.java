package com.inter.clubedafabrica.entities.DTOs;

import java.util.List;

public record OrderResponseDTO(
        Long id,
        String createdAt,
        Double totalAmount,
        String status,
        String userName,
        String userEmail,
        String pickupDate,
        String pickupTime,
        List<OrderItemResponseDTO> items
) {}
