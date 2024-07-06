package ru.clevertec.check;

import ru.clevertec.check.core.*;
import ru.clevertec.check.models.CheckInfo;
import ru.clevertec.check.models.TransactionInfo;

import java.io.IOException;

public class CheckRunner {
    public static void main(String... args) throws IOException {
        CommandLineArgumentsParser parser = new CommandLineArgumentsParser(args);
        TransactionInfo transactionInfo = parser.parse();
        TransactionManager transactionManager = new TransactionManager();
        CheckInfo checkInfo = transactionManager.process(transactionInfo);
        CheckManager checkManager = new CheckManager();
        checkManager.process(checkInfo);
    }
}
