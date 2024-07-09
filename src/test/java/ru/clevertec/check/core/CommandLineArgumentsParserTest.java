package ru.clevertec.check.core;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.TransactionInfo;

class CommandLineArgumentsParserTest {

    @Test
    void parseTest() {
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setError(Error.BAD_REQUEST);
        transactionInfo.addIdQuantityPair(2, 10);
        transactionInfo.addIdQuantityPair(3, 1);

        CommandLineArgumentsParser commandLineArgumentsParser = new CommandLineArgumentsParser(new String[]{"3-1", "2-3", "2-10"});
        TransactionInfo transactionInfo1 = commandLineArgumentsParser.parse();

        Assertions.assertEquals(transactionInfo, transactionInfo1);
    }

    @Test
    void parseTestPositive() {
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setError(Error.NO_ERROR);
        transactionInfo.addIdQuantityPair(2, 10);
        transactionInfo.addIdQuantityPair(3, 1);

        CommandLineArgumentsParser commandLineArgumentsParser = new CommandLineArgumentsParser(new String[]{"3-1", "2-3", "2-10"});
        TransactionInfo transactionInfo1 = commandLineArgumentsParser.parse();

        Assertions.assertNotEquals(transactionInfo, transactionInfo1);
    }
}