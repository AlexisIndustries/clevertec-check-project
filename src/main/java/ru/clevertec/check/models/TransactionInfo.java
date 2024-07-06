package ru.clevertec.check.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter
@ToString
public class TransactionInfo {
    private HashMap<Integer, Integer> idQuantityPairs;
    private int discountCard;
    private double balanceDebitCard;
    private Error error;

    public TransactionInfo() {
        this.idQuantityPairs = new HashMap<>();
        this.error = Error.NO_ERROR;
    }

    public void addIdQuantityPair(int id, int quantity) {
        idQuantityPairs.put(id, quantity);
    }
}
