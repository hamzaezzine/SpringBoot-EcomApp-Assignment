package com.api.EcomApp.repository;

import com.api.EcomApp.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}