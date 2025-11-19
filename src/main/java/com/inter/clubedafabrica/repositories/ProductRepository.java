package com.inter.clubedafabrica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inter.clubedafabrica.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

