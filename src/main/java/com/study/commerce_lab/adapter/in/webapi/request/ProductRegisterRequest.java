package com.study.commerce_lab.adapter.in.webapi.request;

import jakarta.validation.constraints.*;

public record ProductRegisterRequest(
    @NotBlank(message = "SKU는 필수 입력값입니다.")
    @Size(min = 4, max = 20, message = "SKU는 4자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU는 대문자, 숫자, 하이픈만 사용할 수 있습니다.")
    String sku,

    @NotBlank(message = "상품명은 필수 입력값입니다.")
    @Size(min = 2, max = 60, message = "상품명은 2자 이상 60자 이하여야 합니다.")
    String name,

    @Min(value = 100, message = "가격은 100원 이상이어야 합니다.")
    @Max(value = 100_000_000, message = "가격은 100,000,000원 이하여야 합니다.")
    int price,

    @Min(value = 0, message = "재고는 0개 이상이어야 합니다.")
    @Max(value = 1_000_000, message = "재고는 1,000,000개 이하여야 합니다.")
    int stockQuantity){

    public ProductRegisterRequest{
        if (name != null) {
            name = name.trim();
        }
    }
}
