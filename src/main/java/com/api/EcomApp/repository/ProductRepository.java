package com.api.EcomApp.repository;

import com.api.EcomApp.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {
}