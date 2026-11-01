package com.study.commerce_lab.application.required;

import com.study.commerce_lab.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
