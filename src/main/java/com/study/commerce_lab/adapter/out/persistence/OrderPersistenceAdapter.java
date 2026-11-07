package com.study.commerce_lab.adapter.out.persistence;

import com.study.commerce_lab.application.port.out.OrderRepository;
import com.study.commerce_lab.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderPersistenceAdapter implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Optional<Order> findByIdWithItems(Long orderId) {
        return orderJpaRepository.findByIdWithItems(orderId);
    }

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }
}
