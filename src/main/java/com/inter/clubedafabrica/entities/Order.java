package com.inter.clubedafabrica.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Profile user;

    @Column(nullable = false)
    private String status;


    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;

    @Column(name = "pickup_date", nullable = false)
    private String pickupDate;

    @Column(name = "pickup_time", nullable = false)
    private String pickupTime;
    
        // ============================
    // TOTAL DO PEDIDO (SOMA ITENS)
    // ============================
    public double getTotal() {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }

        return items.stream()
                .filter(Objects::nonNull)
                .mapToDouble(item -> {
                    if (item.getTotal() != null) {
                        return item.getTotal();
                    }

                    double qty = item.getQuantity() != null ? item.getQuantity() : 0;
                    double unit = item.getUnitPrice() != null ? item.getUnitPrice() : 0.0;

                    return qty * unit;
                })
                .sum();
    }


    
}
