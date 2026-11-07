package com.study.commerce_lab.adapter.in.webapi.reponse;

import com.study.commerce_lab.domain.order.OrderItem;

public record CreateOrderItemResponse(
        Long productId,
        int quantity,
        long unitPrice,
        long lineAmount
) {

    public static CreateOrderItemResponse from(OrderItem orderItem) {
        return new CreateOrderItemResponse(
                orderItem.getProduct().getId(), orderItem.getQuantity(), orderItem.getUnitPrice(), orderItem.getLineAmount());
    }
}
