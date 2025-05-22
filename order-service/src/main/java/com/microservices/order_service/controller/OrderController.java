package com.microservices.order_service.controller;

import com.microservices.order_service.model.Order;
import com.microservices.order_service.service.OrderService;
import com.microservices.order_service.service.UserService;
import com.microservices.order_service.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    public static class OrderRequestWithAuth {
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

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequestWithAuth request) {
        boolean loggedIn = userService.login(request.getUsername(), request.getPassword());
        if (!loggedIn) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }

        Optional<Order> result = orderService.placeOrder(request.getOrder());
        if (result.isEmpty()) {
            return ResponseEntity.badRequest().body("Not enough stock.");
        }
        return ResponseEntity.ok(result.get());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}/user-info")
    public ResponseEntity<?> getUserInfoByOrder(@PathVariable Long orderId) {
        var userInfoOpt = orderService.getUserInfoByOrder(orderId);
        if (userInfoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userInfoOpt.get());
    }
}
