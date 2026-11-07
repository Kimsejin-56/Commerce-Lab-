package com.study.commerce_lab.adapter.in.webapi.reponse;

import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.enums.OrderStatus;

import java.util.List;

public record OrderResponse(
        Long orderId,
        OrderStatus status,
        long totalAmount,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount(), items);
    }
}
