package com.study.commerce_lab.adapter.out.persistence;

import com.study.commerce_lab.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
