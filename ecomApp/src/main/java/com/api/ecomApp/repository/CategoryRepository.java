package com.api.ecomApp.repository;

import com.api.ecomApp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository  extends JpaRepository<Category, Long> {
}