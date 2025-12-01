package com.inter.clubedafabrica.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inter.clubedafabrica.entities.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Lista itens de um pedido
    List<OrderItem> findByOrder_Id(Long orderId);

    // Lista itens por vários pedidos
    List<OrderItem> findByOrder_IdIn(List<Long> orderIds);

    // Lista itens pelo ID dos itens (usado pelo /items)
    List<OrderItem> findByIdIn(List<Long> ids);
}

