CREATE TABLE product
(
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    sku            VARCHAR(255) NOT NULL,
    name           VARCHAR(255) NOT NULL,
    price          INT          NOT NULL,
    stock_quantity INT          NOT NULL,

    CONSTRAINT pk_product PRIMARY KEY (id),
    CONSTRAINT uk_product_sku UNIQUE (sku)
);
