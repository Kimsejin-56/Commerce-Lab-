package com.study.commerce_lab.domain.product;

import com.study.commerce_lab.domain.BaseEntity;
import com.study.commerce_lab.adapter.in.webapi.request.ProductRegisterRequest;
import com.study.commerce_lab.domain.product.exception.InsufficientStockException;
import com.study.commerce_lab.domain.product.exception.StockQuantityExceededException;
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

    public void increaseStockQuantity(int stockQuantity){
        validateQuantity(stockQuantity);

        if(this.stockQuantity + stockQuantity > 1000000)
            throw new StockQuantityExceededException("재고 수량 초과입니다.");

        this.stockQuantity+=stockQuantity;
    }

    public void decreaseStockQuantity(int stockQuantity){
        validateQuantity(stockQuantity);

        if (this.stockQuantity < stockQuantity)
            throw new InsufficientStockException("재고가 부족합니다.");

        this.stockQuantity-=stockQuantity;
    }

    private void validateQuantity(int quantity) {
        if (quantity < 1 || quantity > 1_000_000) {
            throw new IllegalArgumentException("변경 수량은 1 이상 1,000,000 이하여야 합니다.");
        }
    }
}
