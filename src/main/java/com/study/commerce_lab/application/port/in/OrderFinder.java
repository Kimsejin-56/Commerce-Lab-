package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.order.Order;

public interface OrderFinder {
    Order findOrderById(Long orderId);
}
