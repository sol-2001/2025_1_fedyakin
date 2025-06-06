package ru.shift.workers;

import ru.shift.data.Storage;

public class Consumer implements Runnable {
    private final static long NANOS_IN_MILLI = 1_000_000L;

    private final int consumerId;
    private final Storage storage;
    private final int consumerTime;

    public Consumer(int consumerId, Storage storage, int consumerTime) {
        this.consumerId = consumerId;
        this.storage = storage;
        this.consumerTime = consumerTime;
    }

    @Override
    public void run() {
        long consumerTimeNs = consumerTime * NANOS_IN_MILLI;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long start = System.nanoTime();

                storage.get(consumerId);

                long elapsedTime = System.nanoTime() - start;
                long sleep = consumerTimeNs - elapsedTime;
                if (sleep > 0) {
                    Thread.sleep(sleep / NANOS_IN_MILLI, (int) (sleep % NANOS_IN_MILLI));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(e.getMessage());
        }
    }
}
