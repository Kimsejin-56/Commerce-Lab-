package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.request.OrderRequest;

public interface OrderCreater {
    Order createOrder(OrderRequest request);
}
