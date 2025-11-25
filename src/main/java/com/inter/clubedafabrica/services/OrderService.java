package com.inter.clubedafabrica.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.OrderItem;
import com.inter.clubedafabrica.entities.DTOs.OrderItemDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderRequestDTO;
import com.inter.clubedafabrica.repositories.OrderItemRepository;
import com.inter.clubedafabrica.repositories.OrderRepository;
import com.inter.clubedafabrica.entities.DTOs.OrderResponseDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderItemResponseDTO;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    public Order createOrder(OrderRequestDTO dto) {

        // Calcular total do pedido
        double total = dto.items().stream()
                .mapToDouble(i -> i.unitPrice() * i.quantity())
                .sum();

        // Criar entidade Order
        Order order = new Order();
        order.setUserId(dto.userId());
        order.setStatus("pending");
        order.setTotalAmount(total);
        order.setCreatedAt(LocalDateTime.now());

        // Salvar pedido
        Order savedOrder = orderRepository.save(order);

        // Salvar itens
        for (OrderItemDTO i : dto.items()) {

            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setProductId(i.productId());
            item.setQuantity(i.quantity());
            item.setUnitPrice(i.unitPrice());
            item.setTotal(i.quantity() * i.unitPrice());

            itemRepository.save(item);
        }

        return savedOrder;
    }

    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
    List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

    return orders.stream()
        .map(order -> {
            // Buscar itens do pedido
            List<OrderItem> items = itemRepository.findByOrderId(order.getId());

            List<OrderItemResponseDTO> itemDTOs = items.stream()
                .map(item -> new OrderItemResponseDTO(
                    item.getId(),
                    item.getProductId(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotal(),
                    "Produto " + item.getProductId(), // placeholder, depois ligamos na tabela de produtos
                    null // sem imagem por enquanto
                ))
                .toList();

            return new OrderResponseDTO(
                order.getId(),
                order.getUserId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                itemDTOs
            );
        })
        .toList();
    }

    public List<OrderResponseDTO> getAllOrders() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllOrders'");
    }

    


}
