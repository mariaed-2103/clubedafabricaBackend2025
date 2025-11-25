package com.inter.clubedafabrica.repositories;

import com.inter.clubedafabrica.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}
