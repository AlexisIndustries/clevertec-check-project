package ru.clevertec.check.models;

import lombok.*;

import java.util.HashMap;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TransactionInfo {
    private HashMap<Integer, Integer> idQuantityPairs;
    private int discountCard;
    private double balanceDebitCard;
    private String pathToFile;
    private String saveToFile;
    private String dataSourceUrl;
    private String dataSourceUsername;
    private String dataSourcePassword;
    private Error error;

    public TransactionInfo() {
        this.idQuantityPairs = new HashMap<>();
        this.error = Error.NO_ERROR;
    }

    public void addIdQuantityPair(int id, int quantity) {
        idQuantityPairs.put(id, quantity);
    }
}
