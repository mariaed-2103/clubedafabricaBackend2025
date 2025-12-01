package com.inter.clubedafabrica.entities.DTOs;

import java.util.List;

public record OrderRequestDTO(
        Long userId,
        String pickupDate,
        String pickupTime,
        List<Item> items
) {
    public static record Item(
            Long productId,
            int quantity,
            Double unitPrice
    ) {}
}
