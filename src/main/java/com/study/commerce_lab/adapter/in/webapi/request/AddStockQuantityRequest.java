package com.study.commerce_lab.adapter.in.webapi.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddStockQuantityRequest(
        @NotNull(message = "수량은 필수 입력값입니다.")
        @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
        @Max(value = 1_000_000, message = "수량은 1,000,000 이하이어야 합니다.")
        int quantity
) {
}
