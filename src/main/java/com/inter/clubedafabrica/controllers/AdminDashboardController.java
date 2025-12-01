package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.OrderItem;
import com.inter.clubedafabrica.entities.Product;
import com.inter.clubedafabrica.repositories.OrderItemRepository;
import com.inter.clubedafabrica.repositories.OrderRepository;
import com.inter.clubedafabrica.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    // ============================
    // 1. STATUS GERAL DO DASHBOARD
    // ============================

    @GetMapping("/stats")
    public ResponseEntity<?> getGeneralStats() {

        long totalOrders = orderRepository.count();

        double totalRevenue = orderRepository.findAll()
                .stream()
                .mapToDouble(o -> o.getTotalAmount() != null ? o.getTotalAmount() : 0.0)
                .sum();

        double ticketMean = totalOrders == 0 ? 0 : (totalRevenue / totalOrders);

        // Removido activeUsers (não existe UserRepository)
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", totalOrders);
        stats.put("totalRevenue", totalRevenue);
        stats.put("averageTicket", ticketMean);
        long activeUsers = orderRepository.findAll().stream()
        .filter(o -> o.getUser() != null)
        .map(o -> o.getUser().getId())
        .distinct()
        .count();

        stats.put("activeUsers", activeUsers);

        return ResponseEntity.ok(stats);
    }


    // ============================
    // 2. VENDAS MENSAIS
    // ============================

    @GetMapping("/sales/monthly")
    public ResponseEntity<?> getMonthlySales() {

        Map<String, Double> monthly = new HashMap<>();

        for (Order order : orderRepository.findAll()) {
            if (order.getCreatedAt() != null) {
                int month = order.getCreatedAt().getMonthValue();
                double amount = order.getTotalAmount() != null ? order.getTotalAmount() : 0.0;

                monthly.put(
                        String.valueOf(month),
                        monthly.getOrDefault(String.valueOf(month), 0.0) + amount
                );
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();

        monthly.forEach((month, value) -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("month", month);
            entry.put("value", value);
            result.add(entry);
        });

        result.sort(Comparator.comparing(m -> Integer.parseInt((String)m.get("month"))));

        return ResponseEntity.ok(result);
    }


    // ============================
    // 3. TOP PRODUTOS
    // ============================

    @GetMapping("/products/top")
public ResponseEntity<?> getTopProducts() {

    Map<Long, Integer> productSales = new HashMap<>();

    for (OrderItem item : orderItemRepository.findAll()) {
        if (item.getProduct() != null) {
            Long productId = item.getProduct().getId();
            productSales.put(
                    productId,
                    productSales.getOrDefault(productId, 0) + item.getQuantity()
            );
        }
    }

    // Criamos uma NOVA lista final (não será reatribuída)
    List<Map<String, Object>> result = productSales.entrySet()
            .stream()
            .map(e -> {
                Product product = productRepository.findById(e.getKey()).orElse(null);

                Map<String, Object> obj = new HashMap<>();
                obj.put("name", product != null ? product.getName() : "Desconhecido");
                obj.put("value", e.getValue());
                obj.put("color", "#00AEEF");

                return obj;
            })
            .sorted((a, b) -> ((int) b.get("value") - (int) a.get("value")))
            .limit(5)
            .collect(Collectors.toList()); // coletamos direto para a variável final

    return ResponseEntity.ok(result);
}

}
