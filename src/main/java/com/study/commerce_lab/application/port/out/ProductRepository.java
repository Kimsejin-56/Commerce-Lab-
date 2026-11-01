package com.study.commerce_lab.application.port.out;

import com.study.commerce_lab.domain.product.Product;

import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findBySku(String sku);
}
