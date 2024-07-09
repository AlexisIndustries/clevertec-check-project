package ru.clevertec.check.core;

import org.junit.jupiter.api.Test;
import ru.clevertec.check.models.CheckInfo;
import ru.clevertec.check.models.Error;
import ru.clevertec.check.models.TransactionInfo;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {

    @Test
    void process() throws IOException, SQLException {
        CheckInfo checkInfo = CheckInfo.builder().error(Error.BAD_REQUEST).build();
        CommandLineArgumentsParser commandLineArgumentsParser = new CommandLineArgumentsParser(new String[]{"3-1", "2-3", "2-10"});
        TransactionInfo transactionInfo1 = commandLineArgumentsParser.parse();
        TransactionManager manager = new TransactionManager();
        CheckInfo checkInfo1 = manager.process(transactionInfo1);
        assertEquals(checkInfo1.getError(), checkInfo.getError());
    }
}