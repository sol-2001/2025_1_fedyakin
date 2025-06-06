package ru.shift.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ServerConfig {
    private static final Logger log = LoggerFactory.getLogger(ServerConfig.class);

    private static final String FILE = "/server.properties";
    private static final String CONFIG_KEY_PORT = "server.port";
    private static final String DEFAULT_PORT = "5555";
    private final int port;

    private ServerConfig() {
        Properties p = new Properties();

        try (InputStream in = getClass().getResourceAsStream(FILE)) {
            if (in == null) {
                log.error("Конфиг {} не найден в classpath", FILE);
                throw new IllegalStateException("Не найден " + FILE + " в classpath");
            }
            p.load(in);
        } catch (IOException ioe) {
            log.error("Ошибка чтения конфига {}", FILE, ioe);
            throw new RuntimeException("Ошибка чтения " + FILE, ioe);
        }

        port = Integer.parseInt(p.getProperty(CONFIG_KEY_PORT, DEFAULT_PORT));
    }

    private static final ServerConfig INSTANCE = new ServerConfig();

    public static ServerConfig get() {
        return INSTANCE;
    }

    public int getPort() {
        return port;
    }
}
