package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.product.Product;

public interface ProductFinder {
    Product findProductById(Long id);
}
