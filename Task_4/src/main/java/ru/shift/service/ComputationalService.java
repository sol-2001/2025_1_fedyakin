package ru.shift.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.Task;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ComputationalService {
    private static final Logger log = LoggerFactory.getLogger(ComputationalService.class);
    private static final MathContext MC = new MathContext(34, RoundingMode.HALF_UP);

    private final int countThreads;

    public ComputationalService() {
        countThreads = Runtime.getRuntime().availableProcessors();
    }

    public ComputationalService(int countThreads) {
        this.countThreads = countThreads;
    }

    public BigDecimal compute(long number) {
        log.info("Используется потоков: {}", countThreads);

        long chunkSize = (number + countThreads - 1) / countThreads;
        ExecutorService pool = Executors.newFixedThreadPool(countThreads);
        List<Future<BigDecimal>> futures = new ArrayList<>();

        for (long start = 1; start <= number; start += chunkSize) {
            long end = Math.min(start + chunkSize - 1, number);
            futures.add(pool.submit(new Task(start, end, MC)));
        }

        BigDecimal result = BigDecimal.ZERO;

        try {
            for (var f : futures) {
                result = result.add(f.get(), MC);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Поток был прерван", ie);
        } catch (ExecutionException ee) {
            throw new RuntimeException("Задача завершилась с ошибкой", ee);
        } finally {
            pool.shutdown();
        }

        return result;
    }
}
