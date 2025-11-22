package com.inter.clubedafabrica.entities.DTOs;

public record OrderItemDTO(
    Long productId,
    Integer quantity,
    Double unitPrice
) {

}
