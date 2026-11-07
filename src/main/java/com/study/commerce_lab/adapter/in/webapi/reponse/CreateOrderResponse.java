package com.study.commerce_lab.adapter.in.webapi.reponse;

import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.enums.OrderStatus;

import java.util.List;

public record CreateOrderResponse(
        Long orderId,
        OrderStatus status,
        long totalAmount,
        List<CreateOrderItemResponse> items
) {
    public static CreateOrderResponse from(Order order) {
        List<CreateOrderItemResponse> items = order.getItems().stream()
                .map(CreateOrderItemResponse::from)
                .toList();

        return new CreateOrderResponse(order.getId(), order.getStatus(), order.getTotalAmount(), items);
    }
}
