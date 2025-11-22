package com.inter.clubedafabrica.entities.DTOs;

import java.util.List;

public record OrderRequestDTO(
     Long userId,
     List<OrderItemDTO> items
) {

}
