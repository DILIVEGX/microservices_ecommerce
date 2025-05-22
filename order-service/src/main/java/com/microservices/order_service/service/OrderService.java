package com.microservices.order_service.service;

import com.microservices.order_service.client.PaymentClient;
import com.microservices.order_service.client.ProductClient;
import com.microservices.order_service.client.UserClient;
import com.microservices.order_service.model.Order;
import com.microservices.order_service.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class OrderService {
    private static final String PRODUCT_SERVICE = "product-service";
    private static final String PAYMENT_SERVICE = "payment-service";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private UserClient userClient;

    @CircuitBreaker(name = PRODUCT_SERVICE, fallbackMethod = "productStockFallback")
    public ProductClient.ProductStockResponse getProductStock(Long productId) {
        System.out.println("🔍 Llamando a productClient.getProductStock con id: " + productId);
        return productClient.getProductStock(productId);
    }

    public ProductClient.ProductStockResponse productStockFallback(Long productId, Throwable t) {
        ProductClient.ProductStockResponse fallbackResponse = new ProductClient.ProductStockResponse();
        fallbackResponse.setStock(0);//Stock 0 if fallback
        return fallbackResponse;
    }

    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = "paymentFallback")
    public PaymentClient.PaymentResponse createPayment(PaymentClient.PaymentRequest request) {
        return paymentClient.createPayment(request);
    }

    public PaymentClient.PaymentResponse paymentFallback(PaymentClient.PaymentRequest request, Throwable t) {
        return new PaymentClient.PaymentResponse(request.getOrderId(), "FAILED");
    }

    public Optional<Order> placeOrder(Order orderRequest) {
        // Stock Validation
        ProductClient.ProductStockResponse productStock = getProductStock(orderRequest.getProductId());
        if (productStock.getStock() < orderRequest.getQuantity()) {
            return Optional.empty();
        }

        // Save order status "PENDING"
        orderRequest.setStatus("PENDING");
        Order savedOrder = orderRepository.save(orderRequest);

        // Create payment
        PaymentClient.PaymentRequest paymentRequest = new PaymentClient.PaymentRequest();
        paymentRequest.setOrderId(savedOrder.getId());
        paymentRequest.setAmount(calculateAmount(orderRequest));

        PaymentClient.PaymentResponse paymentResponse = createPayment(paymentRequest);

        //Decrease product stock
        if (paymentResponse.getStatus().equalsIgnoreCase("APPROVED")) {
            productClient.decreaseStock(orderRequest.getProductId(), Map.of("quantity", orderRequest.getQuantity()));
        }

        // Update status order
        savedOrder.setStatus(paymentResponse.getStatus().equalsIgnoreCase("APPROVED") ? "COMPLETED" : "FAILED");
        orderRepository.save(savedOrder);

        return Optional.of(savedOrder);
    }

    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<UserClient.UserInfo> getUserInfoByOrder(Long orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }
        UserClient.UserInfo userInfo = userClient.getUserInfo(orderOpt.get().getUserId());
        return Optional.of(userInfo);
    }

    private Double calculateAmount(Order order) {
        // Simple logic to calculate total
        return 100.0 * order.getQuantity();
    }
}
