package com.study.commerce_lab.application.service;

import com.study.commerce_lab.application.port.in.OrderCreater;
import com.study.commerce_lab.application.port.out.OrderRepository;
import com.study.commerce_lab.application.port.out.ProductRepository;
import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.OrderItem;
import com.study.commerce_lab.domain.order.request.CreateOrderItemRequest;
import com.study.commerce_lab.domain.order.request.CreateOrderRequest;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderModifyService  implements OrderCreater {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(CreateOrderRequest request) {
        Order order = Order.create();

        for(CreateOrderItemRequest item : request.items()){
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId()));

            product.decreaseStockQuantity(item.quantity());

            order.addItem(product, item.quantity());
        }

        return orderRepository.save(order);
    }
}
