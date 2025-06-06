package ru.shift.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public final class GracefulCloser {
    private static final Logger log = LoggerFactory.getLogger(GracefulCloser.class);

    public static void close(ServerSocket ss, ExecutorService pool) {
        try {
            if (ss != null && !ss.isClosed()) {
                ss.close();
            }
        } catch (Exception e) {
            log.error("Ошибка при закрытии ServerSocket", e);
        }

        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.warn("Прерывание при ожидании завершения пула потоков", e);
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void closeQuietly(Closeable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (IOException ioe) {
            log.error("Ошибка при закрытии {}", c.getClass().getSimpleName(), ioe);
        }
    }

    private GracefulCloser() {
    }
}
