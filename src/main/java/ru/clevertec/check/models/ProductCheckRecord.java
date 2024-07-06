package ru.clevertec.check.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ProductCheckRecord {
    private int quantity;
    private String description;
    private double price;
    private double discount;
    private double total;
}
