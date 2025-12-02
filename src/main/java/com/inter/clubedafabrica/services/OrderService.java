package com.inter.clubedafabrica.services;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.OrderItem;
import com.inter.clubedafabrica.entities.Product;
import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.entities.DTOs.CustomerDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderItemDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderItemResponseDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderRequestDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderResponseDTO;
import com.inter.clubedafabrica.repositories.OrderRepository;
import com.inter.clubedafabrica.repositories.OrderItemRepository;
import com.inter.clubedafabrica.repositories.ProductRepository;
import com.inter.clubedafabrica.repositories.ProfileRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ProfileRepository profileRepository;

    public Order createOrder(OrderRequestDTO dto) {

        Profile user = profileRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        double total = dto.items().stream()
                .mapToDouble(i -> i.unitPrice() * i.quantity())
                .sum();

        Order order = new Order();
        order.setUser(user);
        order.setStatus("pending");
        order.setTotalAmount(total);
        order.setCreatedAt(LocalDateTime.now());
        order.setPickupDate(dto.pickupDate());
        order.setPickupTime(dto.pickupTime());

        Order savedOrder = orderRepository.save(order);

        for (OrderRequestDTO.Item i : dto.items()) {

            Product product = productRepository.findById(i.productId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(i.quantity());
            item.setUnitPrice(i.unitPrice());
            item.setTotal(i.unitPrice() * i.quantity());

            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    public List<OrderResponseDTO> getAllOrders() {
    return orderRepository.findAllByOrderByCreatedAtDesc()
        .stream()
        .map(this::mapToDTO)
        .toList();
        }


    public OrderResponseDTO mapToDTO(Order order) {
    var itemDTOs = orderItemRepository.findByOrder_Id(order.getId())
            .stream()
            .map(item -> new OrderItemResponseDTO(
                    item.getId(),
                    item.getProduct().getId(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getTotal(),
                    item.getProduct().getName(),
                    item.getProduct().getImageUrl()
            ))
            .toList();

    return new OrderResponseDTO(
            order.getId(),
            order.getCreatedAt().toString(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getUser().getName(),
            order.getUser().getEmail(),
            order.getPickupDate(),
            order.getPickupTime(),
            itemDTOs
    );
}


    public Order updateStatus(Long id, String newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Não permitir alterar status se já estiver cancelado
        if ("cancelled".equalsIgnoreCase(order.getStatus())) {
        throw new RuntimeException("Não é possível alterar o status de um pedido cancelado");
        }

        if ("completed".equalsIgnoreCase(order.getStatus())) {
        throw new RuntimeException("Não é possível alterar o status de um pedido concluído");
        }

        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
        }


        
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {
        return orderRepository
        .findByUser_IdOrderByCreatedAtDesc(userId)   
        .stream()
        .map(this::mapToDTO)
        .toList();
        }


        public void cancelByUser(Long orderId, Long userId) {
                Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

                //Garante que o pedido é do usuário que está tentando cancelar
                if (!order.getUser().getId().equals(userId)) {
                        throw new RuntimeException("Você não pode cancelar pedidos de outro usuário");
                }

                //Regras: não pode cancelar se já estiver concluído ou cancelado
                if ("completed".equalsIgnoreCase(order.getStatus())
                        || "cancelled".equalsIgnoreCase(order.getStatus())) {
                        throw new RuntimeException("Não é possível cancelar este pedido");
                }

                order.setStatus("cancelled");
                order.setUpdatedAt(LocalDateTime.now());

                orderRepository.save(order);
        }






}
