package ru.clevertec.check.models;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionInfoTest {

    @Test
    public void testTwoIdenticalClassesEquals() {
        TransactionInfo transactionInfo = new TransactionInfo();
        TransactionInfo transactionInfo2 = new TransactionInfo();

        transactionInfo.setError(Error.INTERNAL_SERVER_ERROR);
        transactionInfo2.setError(Error.INTERNAL_SERVER_ERROR);

        assertEquals(transactionInfo, transactionInfo2);
    }

    @Test
    public void testTwoIdenticalClassesNotEquals() {
        TransactionInfo transactionInfo = new TransactionInfo();
        TransactionInfo transactionInfo2 = new TransactionInfo();

        transactionInfo.setError(Error.INTERNAL_SERVER_ERROR);
        transactionInfo2.setError(Error.NO_ERROR);

        assertNotEquals(transactionInfo, transactionInfo2);
    }

    @Test
    public void testTwoClassesPairsEquals() {
        TransactionInfo transactionInfo = new TransactionInfo();
        TransactionInfo transactionInfo2 = new TransactionInfo();

        transactionInfo.addIdQuantityPair(123, 12);
        transactionInfo2.addIdQuantityPair(123, 12);

        assertEquals(transactionInfo, transactionInfo2);
    }

    @Test
    public void testTwoClassesPairsNotEquals() {
        TransactionInfo transactionInfo = new TransactionInfo();
        TransactionInfo transactionInfo2 = new TransactionInfo();

        transactionInfo.addIdQuantityPair(1, 12);
        transactionInfo2.addIdQuantityPair(1, 0);

        assertNotEquals(transactionInfo, transactionInfo2);
    }
}