package com.study.commerce_lab.application.service;

import com.study.commerce_lab.application.port.in.OrderFinder;
import com.study.commerce_lab.application.port.out.OrderRepository;
import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderQueryService implements OrderFinder {

    private final OrderRepository orderRepository;

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
