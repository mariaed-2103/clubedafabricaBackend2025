package com.inter.clubedafabrica.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inter.clubedafabrica.entities.Product;
import com.inter.clubedafabrica.repositories.ProductRepository;
import com.inter.clubedafabrica.entities.Order;
import com.inter.clubedafabrica.entities.OrderItem;
import com.inter.clubedafabrica.repositories.OrderItemRepository;
import com.inter.clubedafabrica.repositories.OrderRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;


@Service
public class ProductService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;


    // ================================
    //  MÉTODO PRINCIPAL DE ESTATÍSTICA
    // ================================
    public Map<String, Object> getSalesStatistics() {

        Map<String, Object> response = new HashMap<>();

        // ============================
        // 1. HISTOGRAMA (vendas por dia)
        // ============================
        List<Map<String, Object>> histogram = new ArrayList<>();

        List<Order> paidOrders = orderRepository.findByStatus("concluido");

        Map<LocalDate, Double> dailySales = new HashMap<>();

        for (Order order : paidOrders) {
            LocalDate date = order.getCreatedAt().toLocalDate();
            double value = order.getTotal();

            dailySales.put(date, dailySales.getOrDefault(date, 0.0) + value);
        }

       dailySales.forEach((date, total) -> {
            Map<String, Object> obj = new HashMap<>();
            // eixo X do gráfico
            obj.put("faixa", date.toString());      // ou "2025-11-30", "01/12", etc
            // altura da barra
            obj.put("quantidade", total);          // usando o total vendido no dia
            histogram.add(obj);
        });


        // ============================
        // 2. VENDAS POR MARCA (brands)
        // ============================
        List<Map<String, Object>> brands = new ArrayList<>();

        Map<String, Double> revenuePerBrand = new HashMap<>();

        for (OrderItem item : orderItemRepository.findAll()) {
            Product p = item.getProduct();
            if (p != null) {
                String brand = p.getBrand() == null ? "Sem Marca" : p.getBrand();
                double total = p.getPrice() * item.getQuantity();
                revenuePerBrand.put(brand, revenuePerBrand.getOrDefault(brand, 0.0) + total);
            }
        }

            revenuePerBrand.forEach((brandName, total) -> {
            Map<String, Object> obj = new HashMap<>();
            obj.put("marca", brandName);       // <- bate com BrandRevenue
            obj.put("faturamento", total);     // <- usado pelo <Bar dataKey="faturamento" />
            brands.add(obj);
        });



        // =============================
        // 3. PRODUTO MAIS VENDIDO
        // =============================
        Map<String, Object> mostSold = new HashMap<>();

        Map<Long, Integer> productQuantity = new HashMap<>();

        for (OrderItem item : orderItemRepository.findAll()) {
            if (item.getProduct() != null) {
                Long id = item.getProduct().getId();
                productQuantity.put(id, productQuantity.getOrDefault(id, 0) + item.getQuantity());
            }
        }

        if (!productQuantity.isEmpty()) {
            Long bestId = productQuantity.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get().getKey();

            Product best = productRepository.findById(bestId).orElse(null);

            mostSold.put("name", best != null ? best.getName() : "Indefinido");
            mostSold.put("price", best != null ? best.getPrice() : 0);
            mostSold.put("totalSold", productQuantity.get(bestId));
        }


        // =============================
        // 4. ESTATÍSTICAS DO TICKET
        // =============================
        List<Double> values = paidOrders.stream()
                .map(Order::getTotal)
                .collect(Collectors.toList());

        Map<String, Object> ticket = new HashMap<>();

        if (!values.isEmpty()) {
            double mean = values.stream().mapToDouble(v -> v).average().orElse(0);

            double variance = values.stream()
                    .mapToDouble(v -> Math.pow(v - mean, 2))
                    .average()
                    .orElse(0);

            double stdDev = Math.sqrt(variance);

            double meanDeviation = values.stream()
                    .mapToDouble(v -> Math.abs(v - mean))
                    .average().orElse(0);

            double amplitude = Collections.max(values) - Collections.min(values);

            double coef = (stdDev / mean) * 100;

            ticket.put("mean", mean);
            ticket.put("variance", variance);
            ticket.put("stdDev", stdDev);
            ticket.put("meanDeviation", meanDeviation);
            ticket.put("amplitude", amplitude);
            ticket.put("coefficientOfVariation", coef);
        }


        // =============================
        // MONTAGEM FINAL DO JSON
        // =============================
        response.put("histogram", histogram);
        response.put("brands", brands);
        response.put("mostSold", mostSold);
        response.put("ticket", ticket);

        return response;
    }
}
