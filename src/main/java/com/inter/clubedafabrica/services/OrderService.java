package com.inter.clubedafabrica.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.OrderItem;
import com.inter.clubedafabrica.entities.Product;
import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.entities.DTOs.CustomerDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderItemDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderItemResponseDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderRequestDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderResponseDTO;
import com.inter.clubedafabrica.repositories.OrderItemRepository;
import com.inter.clubedafabrica.repositories.OrderRepository;
import com.inter.clubedafabrica.repositories.ProductRepository;
import com.inter.clubedafabrica.repositories.ProfileRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;
    private final ProductRepository productRepository;
    private final ProfileRepository profileRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository itemRepository,
            ProductRepository productRepository,
            ProfileRepository profileRepository
    ) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
        this.profileRepository = profileRepository;
    }

    // ================================
    // CRIAR PEDIDO
    // ================================
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

        // 🔥 salvar no backend
        order.setPickupDate(dto.pickupDate());
        order.setPickupTime(dto.pickupTime());

        Order savedOrder = orderRepository.save(order);

        for (OrderItemDTO i : dto.items()) {

            Product product = productRepository.findById(i.productId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(i.quantity());
            item.setUnitPrice(i.unitPrice());
            item.setTotal(i.unitPrice() * i.quantity());

            itemRepository.save(item);
        }

        return savedOrder;
    }


    // ================================
    // LISTA PEDIDOS DO USUÁRIO
    // ================================
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {

        List<Order> orders = orderRepository.findByUser_IdOrderByCreatedAtDesc(userId);

        return orders.stream().map(order -> mapToDTO(order)).toList();
    }


    // ================================
    // LISTA TODOS (ADMIN)
    // ================================
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> mapToDTO(order))
                .toList();
    }


    // ================================
    // MUDAR STATUS
    // ================================
    public OrderResponseDTO updateOrderStatus(Long orderId, String newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        order.setStatus(newStatus);
        orderRepository.save(order);

        return mapToDTO(order);
    }

    // ================================
    // FUNÇÃO QUE MONTA O DTO
    // ================================
    private OrderResponseDTO mapToDTO(Order order) {

        List<OrderItemResponseDTO> itemDTOs = itemRepository.findByOrder_Id(order.getId())
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
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                new CustomerDTO(order.getUser().getName(), order.getUser().getEmail()),
                order.getPickupDate(),
                order.getPickupTime(),
                itemDTOs
        );
    }
}
