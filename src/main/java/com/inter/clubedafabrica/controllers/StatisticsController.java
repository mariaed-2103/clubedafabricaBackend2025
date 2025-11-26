package com.inter.clubedafabrica.controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.OrderItem;
import com.inter.clubedafabrica.entities.Product;
import com.inter.clubedafabrica.repositories.OrderRepository;
import com.inter.clubedafabrica.repositories.OrderItemRepository;
import com.inter.clubedafabrica.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StatisticsController {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @GetMapping
    public Map<String, Object> getStatistics() {

        Map<String, Object> stats = new HashMap<>();

        List<Order> completedOrders = orderRepository.findByStatus("completed");
        List<Long> orderIds = completedOrders.stream().map(Order::getId).toList();

        List<OrderItem> items = orderItemRepository.findByOrderIdIn(orderIds);
        List<Product> products = productRepository.findAll();

        // --- HISTOGRAMA ---
        Map<String, Integer> histogram = new HashMap<>();

        for (OrderItem item : items) {
            double price = item.getUnitPrice();
            int quantity = item.getQuantity();

            int rangeStart = (int) (Math.floor(price / 10) * 10);
            int rangeEnd = rangeStart + 10;

            String key = rangeStart + "-" + rangeEnd;

            histogram.put(key, histogram.getOrDefault(key, 0) + quantity);
        }

        stats.put("histogram", histogram);

        // --- FATURAMENTO POR MARCA ---
        Map<String, Double> brandRevenue = new HashMap<>();

        for (OrderItem item : items) {
            Product p = products.stream()
                .filter(prod -> prod.getId().equals(item.getProductId()))
                .findFirst()
                .orElse(null);

            if (p == null) continue;

            String brand = p.getName().split("-")[0].trim();

            double revenue = item.getUnitPrice() * item.getQuantity();

            brandRevenue.put(brand, brandRevenue.getOrDefault(brand, 0.0) + revenue);
        }

        stats.put("brands", brandRevenue);

        // --- PRODUTO MAIS VENDIDO ---
        Map<Long, Integer> productQty = new HashMap<>();

        for (OrderItem item : items) {
            productQty.put(item.getProductId(),
                productQty.getOrDefault(item.getProductId(), 0) + item.getQuantity()
            );
        }

        Long topProductId = productQty.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (topProductId != null) {
            Product top = products.stream()
                    .filter(p -> p.getId().equals(topProductId))
                    .findFirst()
                    .orElse(null);

            stats.put("mostSold", Map.of(
                    "product", top,
                    "quantity", productQty.get(topProductId)
            ));
        }

        // --- TICKET MÉDIO ---
        List<Double> tickets = completedOrders.stream()
                .map(o -> o.getTotalAmount())
                .toList();

        if (!tickets.isEmpty()) {
            double mean = tickets.stream().mapToDouble(Double::doubleValue).average().orElse(0);

            double max = tickets.stream().mapToDouble(Double::doubleValue).max().orElse(0);
            double min = tickets.stream().mapToDouble(Double::doubleValue).min().orElse(0);

            Map<String, Object> ticketStats = new HashMap<>();
            ticketStats.put("mean", mean);
            ticketStats.put("max", max);
            ticketStats.put("min", min);
            ticketStats.put("amplitude", max - min);

            stats.put("ticket", ticketStats);
        }

        return stats;
    }
}

