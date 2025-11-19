package com.inter.clubedafabrica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inter.clubedafabrica.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}

