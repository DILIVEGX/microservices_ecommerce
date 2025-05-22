package com.microservices.order_service.service;

import com.microservices.order_service.client.UserClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {
    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public boolean login(String username, String password) {
        UserClient.AuthRequest authRequest = new UserClient.AuthRequest();
        authRequest.setUsername(username);
        authRequest.setPassword(password);
        try {
            ResponseEntity<Map<String, String>> response = userClient.login(authRequest);
            return response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().containsKey("token");
        } catch (Exception e) {
            return false;
        }
    }
}

