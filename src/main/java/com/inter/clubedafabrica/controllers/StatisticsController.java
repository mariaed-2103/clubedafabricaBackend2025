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
        List<OrderItem> items = orderItemRepository.findByOrder_IdIn(orderIds);

        // ===================== HISTOGRAMA =====================
        List<Map<String, Object>> histogramList = new ArrayList<>();

        Map<String, Integer> histogram = new HashMap<>();

        for (OrderItem item : items) {
            double price = item.getUnitPrice();
            int quantity = item.getQuantity();

            int rangeStart = (int) (Math.floor(price / 10) * 10);
            int rangeEnd = rangeStart + 10;

            String key = rangeStart + "-" + rangeEnd;

            histogram.put(key, histogram.getOrDefault(key, 0) + quantity);
        }

        histogram.forEach((faixa, quantidade) -> {
            histogramList.add(Map.of(
                "faixa", faixa,
                "quantidade", quantidade
            ));
        });

        stats.put("histogram", histogramList);

        // ===================== FATURAMENTO POR "MARCA" (NOME DO PRODUTO) =====================
            List<Map<String, Object>> brandList = new ArrayList<>();
            Map<String, Double> brandRevenue = new HashMap<>();

            for (OrderItem item : items) {
                Product p = item.getProduct();
                if (p == null) continue;

                // usa o NOME do produto como rótulo no gráfico
                String brand;

                if (p.getBrand() != null && !p.getBrand().isBlank()) {
                    // se um dia você preencher brand, usa brand
                    brand = p.getBrand();
                } else {
                    // hoje: usa o nome do produto
                    brand = p.getName();
                }

                double revenue = item.getUnitPrice() * item.getQuantity();

                brandRevenue.put(brand, brandRevenue.getOrDefault(brand, 0.0) + revenue);
            }

            brandRevenue.forEach((marca, faturamento) -> {
                brandList.add(Map.of(
                    "marca", marca,
                    "faturamento", faturamento
                ));
            });

            stats.put("brands", brandList);


        // ===================== PRODUTO MAIS VENDIDO =====================
        Map<Long, Integer> productQty = new HashMap<>();

        for (OrderItem item : items) {
            Product product = item.getProduct();
            if (product == null) continue;

            Long productId = product.getId();

            productQty.put(
                productId,
                productQty.getOrDefault(productId, 0) + item.getQuantity()
            );
        }

        Long topProductId = productQty.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        if (topProductId != null) {
            Product topProduct = productRepository.findById(topProductId).orElse(null);

            Map<String, Object> mostSoldMap = new HashMap<>();
            mostSoldMap.put("name", topProduct != null ? topProduct.getName() : "Indefinido");
            mostSoldMap.put("price", topProduct != null ? topProduct.getPrice() : 0);
            mostSoldMap.put("image_url", topProduct != null ? topProduct.getImageUrl() : null);
            mostSoldMap.put("totalSold", productQty.getOrDefault(topProductId, 0));

            stats.put("mostSold", mostSoldMap);
        }


        // ===================== TICKET =====================
        List<Double> tickets = completedOrders.stream()
                .map(Order::getTotalAmount)
                .toList();

        if (!tickets.isEmpty()) {
            double mean = tickets.stream().mapToDouble(Double::doubleValue).average().orElse(0);

            double variance = tickets.stream()
                    .mapToDouble(v -> Math.pow(v - mean, 2))
                    .average()
                    .orElse(0);

            double stdDev = Math.sqrt(variance);

            double meanDeviation = tickets.stream()
                    .mapToDouble(v -> Math.abs(v - mean))
                    .average()
                    .orElse(0);

            double amplitude = Collections.max(tickets) - Collections.min(tickets);

            double coef = (stdDev / mean) * 100;

            stats.put("ticket", Map.of(
                    "mean", String.format("%.2f", mean),
                    "variance", String.format("%.2f", variance),
                    "stdDev", String.format("%.2f", stdDev),
                    "meanDeviation", String.format("%.2f", meanDeviation),
                    "amplitude", String.format("%.2f", amplitude),
                    "coefficientOfVariation", String.format("%.2f", coef)
            ));
        }

        return stats;
    }

}

