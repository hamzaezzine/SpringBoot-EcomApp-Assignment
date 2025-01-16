package com.api.ecomApp.service;


import com.api.ecomApp.dto.OrderDTO;
import com.api.ecomApp.dto.OrderItemDTO;
import com.api.ecomApp.model.Order;
import com.api.ecomApp.model.OrderItem;
import com.api.ecomApp.model.Product;
import com.api.ecomApp.model.User;
import com.api.ecomApp.repository.OrderRepository;
import com.api.ecomApp.repository.ProductRepository;
import com.api.ecomApp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Create Order
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING"); // Set default status

        List<OrderItem> orderItems = orderDTO.getItems().stream()
                .map(itemDTO -> {
                    Product product = productRepository.findById(itemDTO.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    return OrderItem.builder()
                            .order(order)
                            .product(product)
                            .quantity(itemDTO.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    // Get All Orders (ADMIN)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get Orders by User (USER)
    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Update Order Status (USER)
    public OrderDTO updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status); // Update the status
        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }

    // Convert Order to DTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setStatus(order.getStatus()); // Include status in DTO
        dto.setItems(order.getOrderItems().stream()
                .map(this::convertToOrderItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    // Convert OrderItem to DTO
    private OrderItemDTO convertToOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        return dto;
    }
}