package com.study.commerce_lab.application.port.out;

import com.study.commerce_lab.domain.order.Order;

public interface OrderRepository {

    Order save(Order order);
}
