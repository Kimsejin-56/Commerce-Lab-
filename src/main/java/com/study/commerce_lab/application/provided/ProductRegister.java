package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.adapter.webapi.ProductRegisterResponse;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.ProductRegisterRequest;

public interface ProductRegister {
    ProductRegisterResponse registerProduct(ProductRegisterRequest request);
}
