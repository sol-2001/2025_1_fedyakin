package ru.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.Callable;

public class Task implements Callable<BigDecimal> {

    private static final Logger log = LoggerFactory.getLogger(Task.class);

    private final long start;
    private final long end;
    private final MathContext mc;

    public Task(long start, long end, MathContext mc) {
        this.start = start;
        this.end = end;
        this.mc = mc;
    }

    @Override
    public BigDecimal call() {
        BigDecimal result = BigDecimal.ZERO;
        for (long i = start; i <= end; i++) {
            BigDecimal iBD = BigDecimal.valueOf(i);
            // тут используется ряд (1 / (n*n))
            BigDecimal divided = BigDecimal.ONE.divide(iBD.multiply(iBD, mc), mc);
            result = result.add(divided, mc);
        }
        log.info("{} computed [{} , {}] => {}", Thread.currentThread().getName(), start, end,
                String.format("%.12f", result));
        return result;
    }
}
