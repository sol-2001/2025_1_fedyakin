package ru.shift;

import org.openjdk.jmh.annotations.*;
import ru.shift.service.ComputationalService;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@Fork(value = 1)
public class TaskBenchmark {

    @State(Scope.Benchmark)
    public static class Params {
        @Param({"1000", "10000", "100000", "1000000", "1000000000"})
        long n;
    }

    @State(Scope.Benchmark)
    public static class ThreadsParams {
        @Param({"1", "2", "4", "8", "12"})
        public int threads;
    }

    private ComputationalService service;

    @Setup
    public void init (ThreadsParams params) {
        service = new ComputationalService(params.threads);
    }

    @Benchmark
    public BigDecimal computeSum(Params params) {
        return service.compute(params.n);
    }
}
