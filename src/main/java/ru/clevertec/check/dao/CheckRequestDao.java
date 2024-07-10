package ru.clevertec.check.dao;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckRequestDao {
    private List<ProductQuantityPair> products;
    private int discountCard;
    private double balanceDebitCard;
}
