package com.microservices.product_service.controller;

import com.microservices.product_service.model.Product;
import com.microservices.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public List<Product> getAll() {
        return service.findAll();
    }

    @GetMapping("/stock/{id}")
    public ResponseEntity<Map<String, Integer>> getStock(@PathVariable Long id) {
        Optional<Product> product = service.findById(id);
        return product
                .map(p -> ResponseEntity.ok(Map.of("stock", p.getStock())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/stock/{id}")
    public ResponseEntity<Product> decreaseStock(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        int quantity = body.get("quantity");
        Optional<Product> optionalProduct = service.findById(id);

        if (optionalProduct.isEmpty()) return ResponseEntity.notFound().build();

        Product existing = optionalProduct.get();

        int newStock = existing.getStock() - quantity;
        if (newStock < 0) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        existing.setStock(newStock);
        Product updated = service.save(existing);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return service.save(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return service.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setStock(product.getStock());
                    return ResponseEntity.ok(service.save(existing));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

