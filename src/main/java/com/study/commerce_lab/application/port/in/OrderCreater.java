package com.study.commerce_lab.application.port.in;

import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.request.CreateOrderItemRequest;
import com.study.commerce_lab.domain.order.request.CreateOrderRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderCreater {
    Order createOrder(CreateOrderRequest request);
}
