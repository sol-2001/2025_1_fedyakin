package ru.shift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Object LOCK = new Object();

    private JsonUtil() {
    }

    public static String toJson(Message msg) {
        synchronized (LOCK) {
            try {
                return MAPPER.writeValueAsString(msg);
            } catch (JsonProcessingException jpe) {
                logger.error("Не удалось сериализовать сообщение: {}", msg, jpe);
                return null;
            }
        }
    }

    public static Message fromJson(String json) {
        synchronized (LOCK) {
            try {
                return MAPPER.readValue(json, Message.class);
            } catch (JsonProcessingException jpe) {
                logger.error("Не удалось десериализовать JSON: {}", json, jpe);
                return null;            }
        }
    }
}
