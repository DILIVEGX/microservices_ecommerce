package com.microservices.payment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody PaymentRequest request) {
        // Simple simulation
        String status = (request.getAmount() > 0) ? "APPROVED" : "FAILED";
        PaymentResponse response = new PaymentResponse(request.getOrderId(), status);
        return ResponseEntity.ok(response);
    }

    static class PaymentRequest {
        private Long orderId;
        private double amount;

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }

    static class PaymentResponse {
        private final Long orderId;
        private final String status;

        public PaymentResponse(Long orderId, String status) {
            this.orderId = orderId;
            this.status = status;
        }
        public Long getOrderId() { return orderId; }
        public String getStatus() { return status; }
    }
}

