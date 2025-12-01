package com.inter.clubedafabrica.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_movements")
@Data
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "movement_type", nullable = false, length = 30)
    private String movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_id")
    private Long orderId; // pode ser nulo

    @Column(columnDefinition = "TEXT")
    private String observation; // opcional

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
