package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.adapter.in.webapi.reponse.IncreaseStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.request.IncreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;

public interface ProductRegister {
    Product registerProduct(ProductRegisterRequest request);

    IncreaseStockQuantityResponse increaseStockQuantity(Long productId, IncreaseStockQuantityRequest request);
}
