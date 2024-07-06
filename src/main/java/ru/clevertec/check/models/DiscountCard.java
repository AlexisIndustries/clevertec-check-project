package ru.clevertec.check.models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountCard {
    private int id;
    private int number;
    private int discountAmount;
}
