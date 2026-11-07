package com.study.commerce_lab.domain.order.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "주문 상품은 최소 1개 이상이어야 합니다.")
        @Size(max = 20, message = "주문 상품은 20개 까지 담을 수 있습니다.")
        List<@Valid CreateOrderItemRequest> items
){

}