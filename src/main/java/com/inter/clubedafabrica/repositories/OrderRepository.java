package com.inter.clubedafabrica.repositories;

import com.inter.clubedafabrica.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // ✅ MÉTODO CORRETO — usando o campo REAL "user"
    List<Order> findByUser_Id(Long userId);

    List<Order> findByIdIn(List<Long> ids);

    List<Order> findByStatus(String status);

    // ✅ Lista do usuário ordenada
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
