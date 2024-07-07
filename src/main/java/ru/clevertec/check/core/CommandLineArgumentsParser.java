package ru.clevertec.check.core;

import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.TransactionInfo;
import ru.clevertec.check.utils.DecimalRoundPrecisionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class CommandLineArgumentsParser {
    private final String ID_QUANTITY_REGEX_TEMPLATE = "\\d+-\\d+";
    private final String DISCOUNT_CARD_REGEX_TEMPLATE = "discountCard=....";
    private final String BALANCE_DEBIT_CARD_REGEX_TEMPLATE = "balanceDebitCard=[+-]?[0-9]+([.][0-9]+)?$";
    private final String PATH_TO_FILE_REGEX_TEMPLATE = "pathToFile=[\\s\\S]+";
    private final String SAVE_TO_FILE_REGEX_TEMPLATE = "saveToFile=[\\s\\S]+";

    private final String[] args;

    public CommandLineArgumentsParser(String[] args) {
        this.args = args;
    }

    public TransactionInfo parse() {
        TransactionInfo transactionInfo = new TransactionInfo();
        int discountCardCount = 0;
        int balanceDebitCardCount = 0;
        int pathToFileCount = 0;
        int saveToFileCount = 0;
        for (String arg : args) {
             if (arg.matches(ID_QUANTITY_REGEX_TEMPLATE)) {
                processIdQuantityField(arg, transactionInfo);
             } else if (arg.matches(DISCOUNT_CARD_REGEX_TEMPLATE)) {
                processDiscountCard(arg, transactionInfo);
                discountCardCount++;
             } else if (arg.matches(BALANCE_DEBIT_CARD_REGEX_TEMPLATE)) {
                processBalanceDebitCard(arg, transactionInfo);
                balanceDebitCardCount++;
             } else if (arg.matches(PATH_TO_FILE_REGEX_TEMPLATE)) {
                processPathToFile(arg, transactionInfo);
                 pathToFileCount++;
             }
             else if (arg.matches(SAVE_TO_FILE_REGEX_TEMPLATE)) {
                processSaveToFile(arg, transactionInfo);
                 saveToFileCount++;
             }
        }
        if (discountCardCount > 1 || balanceDebitCardCount > 1 || balanceDebitCardCount == 0 || pathToFileCount > 1 || saveToFileCount > 1 || saveToFileCount == 0 || pathToFileCount == 0) {
            transactionInfo.setError(Error.BAD_REQUEST);
        }
        return transactionInfo;
    }

    private void processSaveToFile(String arg, TransactionInfo transactionInfo) {
        String saveToFilePath = arg.replace("saveToFile=", "");
        transactionInfo.setSaveToFile(saveToFilePath);
    }

    private void processPathToFile(String arg, TransactionInfo transactionInfo) {
        String pathToFilePath = arg.replace("pathToFile=", "");
        transactionInfo.setPathToFile(pathToFilePath);
    }

    private void processBalanceDebitCard(String arg, TransactionInfo transactionInfo) {
        double balanceDebitCard = Double.parseDouble(arg.replace("balanceDebitCard=", ""));
        transactionInfo.setBalanceDebitCard(DecimalRoundPrecisionUtils.round(balanceDebitCard, 2));
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
