package com.inter.clubedafabrica.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "products") // ajuste se sua tabela tiver nome diferente
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock; // aqui está o campo stock — getStock() / setStock() serão gerados

    @ManyToOne
    @JoinColumn(name = "category_id")
    private com.inter.clubedafabrica.entities.Category category;
}
