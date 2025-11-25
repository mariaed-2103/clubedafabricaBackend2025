package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.Product;
import com.inter.clubedafabrica.entities.Category;
import com.inter.clubedafabrica.repositories.ProductRepository;
import com.inter.clubedafabrica.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping
    public Product create(@RequestBody Product product) {
        if (product.getCategory() != null) {
            Long categoryId = product.getCategory().getId();
            Category category = categoryRepository.findById(categoryId).orElse(null);
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    @GetMapping
    public List<Product> list() {
        return productRepository.findAll();
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product updated) {
        return productRepository.findById(id).map(existing -> {

            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setPrice(updated.getPrice());
            existing.setStock(updated.getStock());

            if (updated.getCategory() != null) {
                Long catId = updated.getCategory().getId();
                Category cat = categoryRepository.findById(catId).orElse(null);
                existing.setCategory(cat);
            } else {
                existing.setCategory(null);
            }

            return productRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
