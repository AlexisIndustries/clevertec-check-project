package ru.clevertec.check.core;

import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.TransactionInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class CommandLineArgumentsParser {
    private final String ID_QUANTITY_REGEX_TEMPLATE = "\\d+-\\d+";
    private final String DISCOUNT_CARD_REGEX_TEMPLATE = "discountCard=....";
    private final String BALANCE_DEBIT_CARD_REGEX_TEMPLATE = "balanceDebitCard=[+-]?[0-9]+([.][0-9]+)?$";

    private final String[] args;

    public CommandLineArgumentsParser(String[] args) {
        this.args = args;
    }

    public TransactionInfo parse() {
        TransactionInfo transactionInfo = new TransactionInfo();
        int discountCardCount = 0;
        int balanceDebitCardCount = 0;
        for (String arg : args) {
             if (arg.matches(ID_QUANTITY_REGEX_TEMPLATE)) {
                processIdQuantityField(arg, transactionInfo);
             } else if (arg.matches(DISCOUNT_CARD_REGEX_TEMPLATE)) {
                processDiscountCard(arg, transactionInfo);
                discountCardCount++;
             } else if (arg.matches(BALANCE_DEBIT_CARD_REGEX_TEMPLATE)) {
                processBalanceDebitCard(arg, transactionInfo);
                balanceDebitCardCount++;
             }
        }
        if (discountCardCount > 1 || balanceDebitCardCount > 1 || balanceDebitCardCount == 0) {
            transactionInfo.setError(Error.BAD_REQUEST);
        }
        return transactionInfo;
    }

    private void processBalanceDebitCard(String arg, TransactionInfo transactionInfo) {
        double balanceDebitCard = Double.parseDouble(arg.replace("balanceDebitCard=", ""));
        BigDecimal tmp = BigDecimal.valueOf(balanceDebitCard);
        tmp = tmp.setScale(2, RoundingMode.HALF_UP);
        transactionInfo.setBalanceDebitCard(tmp.doubleValue());
    }

    private void processDiscountCard(String arg, TransactionInfo transactionInfo) {
        int discountCard = Integer.parseInt(arg.replace("discountCard=", ""));
        transactionInfo.setDiscountCard(discountCard);
    }

    private void processIdQuantityField(String idQuantityField, TransactionInfo transactionInfo) {
        String[] numbers = idQuantityField.split(Pattern.quote("-"));
        int id = Integer.parseInt(numbers[0]);
        int quantity = Integer.parseInt(numbers[1]);
        transactionInfo.addIdQuantityPair(id, quantity);
    }
}
