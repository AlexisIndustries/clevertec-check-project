CREATE DATABASE clevertec_check;

CREATE TABLE product (
    id bigserial,
    description varchar(50),
    price decimal,
    quantity_in_stock integer,
    wholesale_product boolean
);

CREATE TABLE discount_card (
    id bigserial,
    number integer,
    amount smallint check ( amount between 0 and 100)
);

COPY product
    FROM 'C:\Users\Alexey Bobrovich\clevertec-check-project\check\src\main\resources\products.csv'
    DELIMITER ';'
    CSV HEADER;

COPY discount_card
    FROM 'C:\Users\Alexey Bobrovich\clevertec-check-project\check\src\main\resources\discountCards.csv'
    DELIMITER ';'
    CSV HEADER;
