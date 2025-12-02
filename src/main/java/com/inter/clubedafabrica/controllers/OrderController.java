package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.DTOs.OrderRequestDTO;
import com.inter.clubedafabrica.entities.DTOs.OrderResponseDTO;
import com.inter.clubedafabrica.services.OrderService;

import com.inter.clubedafabrica.repositories.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;


    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @GetMapping
        public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {

            List<OrderResponseDTO> list = orderRepository.findAll()
                    .stream()
                    .map(orderService::mapToDTO)
                    .toList();

            return ResponseEntity.ok(list);
        }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUser(@PathVariable Long userId) {

        List<OrderResponseDTO> list = orderService.getOrdersByUser(userId);

        return ResponseEntity.ok(list);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body
    ) {
        String newStatus = body.get("status");

        if (newStatus == null || newStatus.isBlank()) {
            return ResponseEntity.badRequest().body("Status é obrigatório");
        }

        try {
            Order updated = orderService.updateStatus(id, newStatus);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Pedido não encontrado");
        }
    }

    @PatchMapping("/{id}/cancel")
        public ResponseEntity<?> cancelOrderByUser(
                @PathVariable Long id,
                @RequestBody Map<String, Long> body
        ) {
            Long userId = body.get("userId");

            if (userId == null) {
                return ResponseEntity.badRequest().body("userId é obrigatório");
            }

            try {
                orderService.cancelByUser(id, userId);
                return ResponseEntity.ok("Pedido cancelado com sucesso!");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }


}
