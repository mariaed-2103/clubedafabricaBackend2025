package com.inter.clubedafabrica.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.OrderItem;
import com.inter.clubedafabrica.entities.DTOs.OrderRequestDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderResponseDTO;

import com.inter.clubedafabrica.services.OrderService;
import com.inter.clubedafabrica.repositories.OrderItemRepository;

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

    @GetMapping("/completed")
    public List<Order> getCompletedOrders() {
        return orderService.getCompletedOrders();
    }

    @PostMapping("/items")
    public List<OrderItem> getOrderItems(@RequestBody List<Long> ids) {
        return orderService.getOrderItems(ids);
    }
}
