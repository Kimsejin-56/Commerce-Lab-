package com.study.commerce_lab.adapter.webapi.reponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductRegisterResponse {
    Long id;
    String sku;
    String name;
    int price;
    int stockQuantity;
}
