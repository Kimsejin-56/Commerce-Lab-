package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.request.DecreaseStockQuantityRequest;
import com.study.commerce_lab.domain.product.request.IncreaseStockQuantityRequest;

public interface ProductStockManager {
    Product increaseStockQuantity(Long productId, IncreaseStockQuantityRequest request);

    Product decreaseStockQuantity(Long productId, DecreaseStockQuantityRequest request);
}
