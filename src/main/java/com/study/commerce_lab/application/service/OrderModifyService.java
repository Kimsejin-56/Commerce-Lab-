package com.study.commerce_lab.application.service;

import com.study.commerce_lab.application.port.in.OrderCreater;
import com.study.commerce_lab.application.port.out.OrderRepository;
import com.study.commerce_lab.application.port.out.ProductRepository;
import com.study.commerce_lab.domain.order.Order;
import com.study.commerce_lab.domain.order.exception.DuplicateOrderProductException;
import com.study.commerce_lab.domain.order.request.CreateOrderItemRequest;
import com.study.commerce_lab.domain.order.request.CreateOrderRequest;
import com.study.commerce_lab.domain.product.Product;
import com.study.commerce_lab.domain.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderModifyService  implements OrderCreater {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public Order createOrder(CreateOrderRequest request) {
        validateDuplicateProduct(request);

        Order order = Order.create();

        for(CreateOrderItemRequest item : request.items()){
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException(item.productId()));

            product.decreaseStockQuantity(item.quantity());

            order.addItem(product, item.quantity());
        }

        return orderRepository.save(order);
    }

    private void validateDuplicateProduct(CreateOrderRequest request) {
        Set<Long> productIds = new HashSet<>();

        for (CreateOrderItemRequest item : request.items()) {
            if (!productIds.add(item.productId())) {
                throw new DuplicateOrderProductException(item.productId());
            }
        }
    }
}
