package ru.shift.exceptions;

public class InvalidConfigException extends RuntimeException {
    public InvalidConfigException(String message) {
        super(message);
    }

    public InvalidConfigException(String message, Throwable c) {
        super(message, c);
    }
}
