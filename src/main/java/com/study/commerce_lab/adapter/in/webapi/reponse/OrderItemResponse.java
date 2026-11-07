package com.study.commerce_lab.adapter.in.webapi.reponse;

import com.study.commerce_lab.domain.order.OrderItem;

public record OrderItemResponse(
        Long productId,
        int quantity,
        long unitPrice,
        long lineAmount
) {

    public static OrderItemResponse from(OrderItem orderItem) {
        return new OrderItemResponse(orderItem.getProduct().getId(), orderItem.getQuantity(), orderItem.getUnitPrice(), orderItem.getLineAmount());
    }
}
