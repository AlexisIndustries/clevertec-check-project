package ru.clevertec.check.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private int id;
    private String description;
    private double price;
    private int quantityInStock;
    private boolean wholesaleProduct;
}
