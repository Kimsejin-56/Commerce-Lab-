package com.study.commerce_lab.adapter.in.webapi.reponse;

import com.study.commerce_lab.domain.product.Product;

public record ProductRegisterResponse(
        Long id,
        String sku,
        String name,
        int price,
        int stockQuantity
) {
    public static ProductRegisterResponse from(Product product) {
        return new ProductRegisterResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
}
