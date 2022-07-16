DROP TABLE IF EXISTS sub_product_category;
DROP TABLE IF EXISTS upper_product_category;

CREATE TABLE upper_product_category
(
    upper_product_category_id BIGINT       NOT NULL AUTO_INCREMENT,
    name                VARCHAR(255) NOT NULL,
    created_datetime    TIMESTAMP,
    modified_datetime   TIMESTAMP,
    PRIMARY KEY (upper_product_category_id)
);

CREATE TABLE sub_product_category
(
    sub_product_category_id       BIGINT NOT NULL AUTO_INCREMENT,
    upper_product_category_id     BIGINT,
    name                 VARCHAR(255),
    created_datetime     TIMESTAMP,
    modified_datetime    TIMESTAMP,
    PRIMARY KEY (sub_product_category_id),
    FOREIGN KEY (upper_product_category_id) REFERENCES upper_product_category (upper_product_category_id)
);
