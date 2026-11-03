package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.adapter.in.webapi.reponse.AddStockQuantityResponse;
import com.study.commerce_lab.adapter.in.webapi.request.AddStockQuantityRequest;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;

public interface ProductRegister {
    Product registerProduct(ProductRegisterRequest request);

    AddStockQuantityResponse addStockQuantity(Long productId, AddStockQuantityRequest request);
}
