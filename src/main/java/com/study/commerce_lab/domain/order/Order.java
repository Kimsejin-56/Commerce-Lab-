package com.study.commerce_lab.domain.order;

import com.study.commerce_lab.domain.BaseEntity;
import com.study.commerce_lab.domain.order.enums.OrderStatus;
import com.study.commerce_lab.domain.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@ToString(callSuper = true)
public class Order extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(nullable = false)
    private long totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "item_position")
    @ToString.Exclude
    private final List<OrderItem> items = new ArrayList<>();

    protected Order() {
        this.totalAmount = 0L;
        this.status = OrderStatus.CREATED;
    }

    public static Order create() {
        return new Order();
    }

    public void addItem(Product product, int quantity) {
        OrderItem orderItem = OrderItem.create(this, product, quantity);

        this.items.add(orderItem);
        this.totalAmount += orderItem.getLineAmount();
    }

}
