package ru.clevertec.check.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class CheckInfo {
    private LocalDateTime checkTime;
    private List<ProductCheckRecord> productCheckRecordList;
    private String pathToFile;
    private String saveToFile;
    private DiscountCard discountCard;
    private double totalPrice;
    private Error error;
}
