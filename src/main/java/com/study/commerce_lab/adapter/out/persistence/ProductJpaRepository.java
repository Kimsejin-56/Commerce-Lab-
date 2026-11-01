package com.study.commerce_lab.adapter.out.persistence;

import com.study.commerce_lab.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ProductJpaRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);
}
