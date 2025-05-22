# Building a Microservices-Based E-Commerce Application
Objectives
By the end of this project, you should be able to:

- Design and develop multiple microservices with Spring Boot.
- Implement inter-service communication using REST APIs.
- Use Spring Cloud Eureka for service discovery.
- Use a centralized configuration server for managing service configurations.
- Understand and implement fault tolerance using Spring Cloud Circuit Breaker (Resilience4J).
- Deploy the services and test their interactions.
---

## Project Setup

### Prerequisites

- Java 24 or higher
- Maven 3.6 or higher
- MySQL server running on port 3306
- An IDE such as IntelliJ IDEA, VSCode, or Eclipse

### Database Setup
- Open your MySQL client and create the databases:

```sql
CREATE DATABASE `MicroservicesOrder`;
CREATE DATABASE `MicroservicesProduct`;
CREATE DATABASE `MicroservicesUser`;
```
# Running the Application
# To run the application:
Open a terminal in the project root folder for each microservice.

Run:
```
mvn clean spring-boot:run
```

Follow this order and the API will start and listen on:
- config-server:http://localhost:8888
- eureka-server:http://localhost:8761
- user-service:http://localhost:8084
- product-service:http://localhost:8081
- payment-service:http://localhost:8083
- order-service:http://localhost:8082
### Order service is the main service.

# API Endpoints and Sample Requests
# Authentication
## - Register

POST /users/register

Request Body (JSON):
```
{
"username": "user1",
"password": "123",
"role": "USER"
}
```

## - Login

POST /users/login

Request Body (JSON):
```
{
"username": "user1",
"password": "password123"
}
```
Expected Response:
```
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```


# Products
## - Create product

POST /products

Request Body (JSON):
```
{
"name": "product",
"description": "example product desc",
"price": 50,
"stock": 20
}
```

# Orders
## - Create order

POST /products

Request Body (JSON):
```
{
  "username": "user1",
  "password": "123",
  "order": {
    "productId": 1,
    "quantity": 1,
    "userId": 1
  }
}
```

