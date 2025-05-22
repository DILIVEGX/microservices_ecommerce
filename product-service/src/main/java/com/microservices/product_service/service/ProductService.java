package com.microservices.product_service.service;

import com.microservices.product_service.model.Product;
import com.microservices.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> findAll() { return repo.findAll(); }
    public Product save(Product p) { return repo.save(p); }
    public Optional<Product> findById(Long id) { return repo.findById(id); }
    public void deleteById(Long id) { repo.deleteById(id); }
}

