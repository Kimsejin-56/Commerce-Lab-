package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;

public interface ProductRegister {
    Product registerProduct(ProductRegisterRequest request);
}
