package ru.shift.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.exceptions.InvalidConfigException;

import java.io.IOException;
import java.util.Properties;

@Getter
public class Config {
    private static final Logger log = LoggerFactory.getLogger(Config.class);
    private static Config INSTANCE;
    private static final String FILE_NAME = "configuration.properties";

    private static final String STORAGE_SIZE = "storageSize";
    private static final String PRODUCER_COUNT = "producerCount";
    private static final String CONSUMER_COUNT = "consumerCount";
    private static final String PRODUCER_TIME = "producerTime";
    private static final String CONSUMER_TIME = "consumerTime";

    private final int storageSize;
    private final int producerCount;
    private final int consumerCount;
    private final int producerTime;
    private final int consumerTime;

    private Config(Properties prop){
        storageSize = parse(prop, STORAGE_SIZE);
        producerCount = parse(prop, PRODUCER_COUNT);
        consumerCount = parse(prop, CONSUMER_COUNT);
        producerTime = parse(prop, PRODUCER_TIME);
        consumerTime = parse(prop, CONSUMER_TIME);

        if (producerCount == 0 && consumerCount == 0) {
            throw new InvalidConfigException("Нужен хотя бы один producer или consumer");
        }
        log.info("Конфигурация загружена: storageSize={}, producers={}, consumers={}",
                storageSize, producerCount, consumerCount);
    }

    public static void initialize() throws InvalidConfigException {
        Properties prop = new Properties();
        try {
            prop.load(Config.class.getClassLoader().getResourceAsStream(FILE_NAME));
        } catch (IOException ioe) {
            throw new InvalidConfigException("Ошибка чтения файла конфигурации: " + FILE_NAME, ioe);
        }
        if (INSTANCE != null) {
            throw new IllegalStateException("Config уже проинициализирован");
        }
        INSTANCE = new Config(prop);
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Config не проинициализирован");
        }
        return INSTANCE;
    }

    private static int parse(Properties prop, String key) throws InvalidConfigException {
        String str = prop.getProperty(key);

        if (str == null)
            throw new InvalidConfigException("Отсутствует параметр \"" + key + '"');

        try {
            int val = Integer.parseInt(str.trim());
            if (val <= 0) throw new NumberFormatException("не положительное");
            return val;
        } catch (NumberFormatException nfe) {
            throw new InvalidConfigException(
                    "Параметр \"" + key + "\" должен быть положительным целым числом, найдено: " + str, nfe);
        }
    }
}
