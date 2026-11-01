package com.study.commerce_lab.domain.product;

import com.study.commerce_lab.domain.BaseEntity;
import com.study.commerce_lab.domain.product.request.ProductRegisterRequest;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Product extends BaseEntity {

    @NaturalId
    String sku;

    String name;

    int price;

    int stockQuantity;

    public static Product create(ProductRegisterRequest request){
        Product product = new Product();

        product.sku=request.sku();
        product.name=request.name();
        product.price=request.price();
        product.stockQuantity=request.stockQuantity();

        return product;
    }
}
