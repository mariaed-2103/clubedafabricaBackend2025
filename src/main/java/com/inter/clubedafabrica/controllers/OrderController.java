package com.inter.clubedafabrica.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.DTOs.OrderRequestDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderResponseDTO;

import com.inter.clubedafabrica.services.OrderService;
import com.inter.clubedafabrica.repositories.OrderItemRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository; 

    // Construtor com os 2 parâmetros
    public OrderController(OrderService orderService, OrderItemRepository orderItemRepository) {
        this.orderService = orderService;
        this.orderItemRepository = orderItemRepository;
    }

    @PostMapping
    public Order createOrder(@RequestBody OrderRequestDTO dto) {
        return orderService.createOrder(dto);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponseDTO> getOrdersByUser(@PathVariable Long userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping
    public List<OrderResponseDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String newStatus = body.get("status");

        if (newStatus == null || newStatus.isBlank()) {
            return ResponseEntity.badRequest().body("Status é obrigatório");
        }

        try {
            OrderResponseDTO updated = orderService.updateOrderStatus(id, newStatus);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
