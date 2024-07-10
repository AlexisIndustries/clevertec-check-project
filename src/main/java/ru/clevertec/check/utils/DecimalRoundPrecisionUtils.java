package ru.clevertec.check.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DecimalRoundPrecisionUtils {
    public static double round(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
