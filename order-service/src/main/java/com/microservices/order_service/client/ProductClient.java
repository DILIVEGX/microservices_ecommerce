package com.microservices.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/products/stock/{id}")
    ProductStockResponse getProductStock(@PathVariable("id") Long id);

    @PutMapping("/products/stock/{id}")
    void decreaseStock(@PathVariable("id") Long id, @RequestBody Map<String, Integer> body);

    class ProductStockResponse {
        private int stock;
        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }
    }
}
