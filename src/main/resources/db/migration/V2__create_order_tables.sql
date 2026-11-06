CREATE TABLE orders
(
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    status       VARCHAR(20)  NOT NULL,
    total_amount BIGINT       NOT NULL,

    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id          BIGINT NOT NULL AUTO_INCREMENT,
    order_id    BIGINT NOT NULL,
    product_id  BIGINT NOT NULL,
    quantity    INT    NOT NULL,
    unit_price  BIGINT NOT NULL,
    line_amount BIGINT NOT NULL,

    CONSTRAINT pk_order_item PRIMARY KEY (id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE INDEX idx_order_item_order_id ON order_item (order_id);
CREATE INDEX idx_order_item_product_id ON order_item (product_id);
