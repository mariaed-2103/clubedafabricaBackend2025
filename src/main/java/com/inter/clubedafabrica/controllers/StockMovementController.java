package com.inter.clubedafabrica.controllers;
import com.inter.clubedafabrica.entities.Product;
import com.inter.clubedafabrica.entities.StockMovement;
import com.inter.clubedafabrica.repositories.ProductRepository;
import com.inter.clubedafabrica.repositories.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@CrossOrigin(origins = "*")
public class StockMovementController {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private ProductRepository productRepository;

    // LISTAR TODAS AS MOVIMENTAÇÕES
    @GetMapping
    public List<StockMovement> getAllMovements() {
        return stockMovementRepository.findAll();
    }

    // CRIAR MOVIMENTAÇÃO
    @PostMapping
    public ResponseEntity<StockMovement> createMovement(@RequestBody StockMovement movement) {

        // validação certa
        if (movement.getProductId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productRepository.findById(movement.getProductId())
                .orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualiza o estoque automaticamente
        if (movement.getMovementType().equals("entrada")) {
            product.setStock(product.getStock() + movement.getQuantity());
        } else if (movement.getMovementType().equals("saida") || movement.getMovementType().equals("quebra")) {
            product.setStock(product.getStock() - movement.getQuantity());
        }

        productRepository.save(product);

        movement.setCreatedAt(LocalDateTime.now());
        StockMovement saved = stockMovementRepository.save(movement);

        return ResponseEntity.ok(saved);
    }

}



