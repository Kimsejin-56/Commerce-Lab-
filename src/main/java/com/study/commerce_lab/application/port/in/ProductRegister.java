package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.Product;

public interface ProductRegister {
    Product registerProduct(ProductRegisterRequest request);
}
