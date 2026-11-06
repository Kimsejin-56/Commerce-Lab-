package com.study.commerce_lab.adapter.in.webapi.reponse;

import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.OrderItem;
import com.study.commerce_lab.domain.order.enums.OrderStatus;

import java.util.List;

public record CreateOrderResponse(
        Long orderId,
        OrderStatus status,
        Long totalAmount,
        List<OrderItem> items
) {
    public static CreateOrderResponse from(Order order) {
        return new CreateOrderResponse(order.getId(), order.getStatus(), order.getTotalAmount(), order.getItems());
    }
}
