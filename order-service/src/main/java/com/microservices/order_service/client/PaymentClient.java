package com.microservices.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    PaymentResponse createPayment(@RequestBody PaymentRequest paymentRequest);

    class PaymentRequest {
        private Long orderId;
        private Double amount;

        public PaymentRequest() {}

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }

        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
    }

    class PaymentResponse {
        private Long orderId;
        private String status;
        public PaymentResponse() {}
        public PaymentResponse(Long orderId, String status) {
            this.orderId = orderId;
            this.status = status;
        }

        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
