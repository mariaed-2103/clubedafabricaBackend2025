package com.inter.clubedafabrica.entities.DTOs;

public record OrderItemResponseDTO(
    Long id,
    Long productId,
    Integer quantity,
    Double unitPrice,
    Double total,
    String productName,
    String imageUrl
) {

}
