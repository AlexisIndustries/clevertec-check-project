package ru.clevertec.check.core;

import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.TransactionInfo;
import ru.clevertec.check.utils.DecimalRoundPrecisionUtils;

import java.util.regex.Pattern;

public class CommandLineArgumentsParser {
    private final String ID_QUANTITY_REGEX_TEMPLATE = "\\d+-\\d+";
    private final String DISCOUNT_CARD_REGEX_TEMPLATE = "discountCard=....";
    private final String BALANCE_DEBIT_CARD_REGEX_TEMPLATE = "balanceDebitCard=[+-]?[0-9]+([.][0-9]+)?$";
    private final String SAVE_TO_FILE_REGEX_TEMPLATE = "saveToFile=[\\s\\S]+";
    private final String DATASOURCE_URL_REGEX_TEMPLATE = "datasource\\.url=[\\s\\S]+";
    private final String DATASOURCE_USERNAME_REGEX_TEMPLATE = "datasource\\.username=[\\s\\S]+";
    private final String DATASOURCE_PASSWORD_REGEX_TEMPLATE = "datasource\\.password=[\\s\\S]+";

    private final String[] args;

    public CommandLineArgumentsParser(String[] args) {
        this.args = args;
    }

    public TransactionInfo parse() {
        TransactionInfo transactionInfo = new TransactionInfo();
        int discountCardCount = 0;
        int balanceDebitCardCount = 0;
        int saveToFileCount = 0;
        int dataSourceUrlCount = 0;
        int dataSourceUsernameCount = 0;
        int dataSourcePasswordCount = 0;
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
             else if (arg.matches(SAVE_TO_FILE_REGEX_TEMPLATE)) {
                processSaveToFile(arg, transactionInfo);
                saveToFileCount++;
             } else if (arg.matches(DATASOURCE_URL_REGEX_TEMPLATE)) {
                 processDatasourceUrl(arg, transactionInfo);
                 dataSourceUrlCount++;
             } else if (arg.matches(DATASOURCE_USERNAME_REGEX_TEMPLATE)) {
                 processDatasourceUsername(arg, transactionInfo);
                 dataSourceUsernameCount++;
             }
             else if (arg.matches(DATASOURCE_PASSWORD_REGEX_TEMPLATE)) {
                 processDatasourcePassword(arg, transactionInfo);
                 dataSourcePasswordCount++;
             }
        }
        if (discountCardCount > 1 || balanceDebitCardCount > 1 || balanceDebitCardCount == 0 || dataSourceUrlCount > 1 || dataSourceUsernameCount > 1 || dataSourcePasswordCount > 1 || dataSourceUsernameCount == 0 || dataSourcePasswordCount == 0 || dataSourceUrlCount == 0 || saveToFileCount > 1 || saveToFileCount == 0) {
            transactionInfo.setError(Error.BAD_REQUEST);
        }
        return transactionInfo;
    }

    private void processDatasourcePassword(String arg, TransactionInfo transactionInfo) {
        String datasourcePassword = arg.replace("datasource.password=", "");
        transactionInfo.setDataSourcePassword(datasourcePassword);
    }

    private void processDatasourceUsername(String arg, TransactionInfo transactionInfo) {
        String datasourceUsername = arg.replace("datasource.username=", "");
        transactionInfo.setDataSourceUsername(datasourceUsername);
    }

    private void processDatasourceUrl(String arg, TransactionInfo transactionInfo) {
        String datasourceUrl = arg.replace("datasource.url=", "");
        transactionInfo.setDataSourceUrl(datasourceUrl);
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
