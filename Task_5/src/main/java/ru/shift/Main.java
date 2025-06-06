package ru.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.config.Config;
import ru.shift.data.Storage;
import ru.shift.exceptions.InvalidConfigException;
import ru.shift.workers.Consumer;
import ru.shift.workers.Producer;

public class Main {

    private static final  Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            Config.initialize();
            Config conf = Config.getInstance();
            Storage storage = new Storage(conf.getStorageSize());

            log.info("Начало производства");
            for (int i = 0; i < conf.getProducerCount(); i++) {
                new Thread(new Producer(i, storage, conf.getProducerTime()), "Producer-" + i)
                        .start();
            }

            for (int i = 0; i < conf.getConsumerCount(); i++) {
                new Thread(new Consumer(i, storage, conf.getConsumerTime()), "Consumer-" + i)
                        .start();
            }

        } catch (InvalidConfigException ice) {
            log.error("Файл конфигурации некорректен: {}", ice.getMessage());
        } catch (IllegalStateException ise) {
            log.error("Ошибка состояния Config: {}", ise.getMessage());
        }
    }
}

