package com.study.commerce_lab.domain.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductRegisterRequest(
    @Size(min = 4, max = 20)String sku,
    @NotBlank @Size(min = 2, max = 60) String name,
    @Size(min = 100, max = 100000000) int price,
    @Size(max = 1000000) int stockQuantity){
}