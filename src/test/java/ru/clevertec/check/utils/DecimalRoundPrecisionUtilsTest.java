package ru.clevertec.check.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DecimalRoundPrecisionUtilsTest {

    @Test
    void roundPrecisionTo2Places() {
        double number = 3.1255;
        double result = DecimalRoundPrecisionUtils.round(number, 2);
        assertEquals(3.13, result);
    }
}