package com.study.commerce_lab.domain.order.request;

import com.study.commerce_lab.domain.order.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "주문 상품은 최소 1개 이상이어야 합니다.")
        List<@Valid CreateOrderItemRequest> items
){

}