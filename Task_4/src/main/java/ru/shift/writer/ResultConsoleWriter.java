package ru.shift.writer;

import java.math.BigDecimal;

// Аналогично с ридером
public class ResultConsoleWriter {
    private static final double LIMIT = Math.PI * Math.PI / 6.0;

    public void printResults(long number, BigDecimal result, long elapsedMs) {
        System.out.printf("Сумма для i=1..%d = %.12f%n", number, result);
        System.out.printf("Ожидаемый предел = %.12f%n", LIMIT);
        System.out.printf("Время вычислений = %d ms%n", elapsedMs);
    }
}
