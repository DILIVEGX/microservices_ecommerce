package com.microservices.order_service.dto;

import com.microservices.order_service.model.Order;

public class OrderRequestWithAuth {
    private String username;
    private String password;
    private Order order;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}

