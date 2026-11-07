package com.study.commerce_lab.adapter.out.persistence;

import com.study.commerce_lab.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "join fetch o.items oi " +
            "join fetch oi.product " +
            "where o.id = :orderId")
    Optional<Order> findByIdWithItems(Long orderId);

}
