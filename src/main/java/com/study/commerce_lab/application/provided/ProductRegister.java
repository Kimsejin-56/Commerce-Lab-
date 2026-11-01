package com.study.commerce_lab.application.provided;

import com.study.commerce_lab.adapter.webapi.reponse.ProductRegisterResponse;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;

public interface ProductRegister {
    ProductRegisterResponse registerProduct(ProductRegisterRequest request);
}
