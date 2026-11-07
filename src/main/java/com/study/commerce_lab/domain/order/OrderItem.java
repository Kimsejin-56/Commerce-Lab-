package com.study.commerce_lab.domain.order;

import com.study.commerce_lab.domain.BaseEntity;
import com.study.commerce_lab.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long unitPrice;

    @Column(nullable = false)
    private long lineAmount;

    private OrderItem(Order order, Product product, int quantity) {
        this.unitPrice = product.getPrice();
        this.quantity = quantity;
        this.product = product;
        this.order = order;
        this.lineAmount = quantity * product.getPrice();
    }

    public static OrderItem create(Order order, Product product, int quantity) {
        return new OrderItem(order, product, quantity);
    }


}
