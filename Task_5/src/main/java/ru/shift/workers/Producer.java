package ru.shift.workers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.data.Resource;
import ru.shift.data.Storage;

public class Producer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Producer.class);
    private final static long NANOS_IN_MILLI = 1_000_000L;

    private final int producerId;
    private final Storage storage;
    private final int producerTime;

    public Producer(int producerId, Storage storage, int producerTime) {
        this.producerId = producerId;
        this.storage = storage;
        this.producerTime = producerTime;
    }

    @Override
    public void run(){
        long producerTimeNs = producerTime * NANOS_IN_MILLI;
        try {
            while (!Thread.currentThread().isInterrupted()) {
                long start = System.nanoTime();

                Resource resources = new Resource();
                storage.put(resources, producerId);

                long elapsedTime = System.nanoTime() - start;
                long sleep = producerTimeNs - elapsedTime;

                if (sleep > 0) {
                    Thread.sleep(sleep / NANOS_IN_MILLI, (int) (sleep % NANOS_IN_MILLI));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Поток Producer-{} прерван", producerId, e);
        }
    }
}