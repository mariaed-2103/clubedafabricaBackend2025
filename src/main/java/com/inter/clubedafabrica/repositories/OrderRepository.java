package com.inter.clubedafabrica.repositories;

import com.inter.clubedafabrica.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<Order> findByIdIn(List<Long> ids);

    List<Order> findByStatus(String status);

    List<Order> findAll();
}
