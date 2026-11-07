package com.study.commerce_lab.domain.order.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull(message = "상품 ID는 필수 입력값입니다.")
        @Min(value = 1, message = "상품 ID는 1 이상이어야 합니다.")
        Long productId,

        @NotNull(message = "주문 수량은 필수 입력값입니다.")
        @Min(value = 1, message = "주문 수량은 1 이상이어야 합니다.")
        @Max(value = 99, message = "주문 수량은 99 이하이어야 합니다.")
        Integer quantity
) {
}
