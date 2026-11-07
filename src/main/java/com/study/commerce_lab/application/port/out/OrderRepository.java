package com.study.commerce_lab.application.port.out;

import com.study.commerce_lab.domain.order.Order;

import java.util.Optional;

public interface OrderRepository {
    Optional<Order> findById(Long orderId);

    Optional<Order> findByIdWithItems(Long orderId);

    Order save(Order order);
}
