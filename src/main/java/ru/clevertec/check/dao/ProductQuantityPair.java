package ru.clevertec.check.dao;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductQuantityPair {
    private int id;
    private int quantity;
}
