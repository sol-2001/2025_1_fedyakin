package ru.shift.exception;

import java.io.IOException;

public class OutputException extends IOException {
    public OutputException(String message) {
        super(message);
    }

    public OutputException(String message, Throwable cause) {
        super(message, cause);
    }
}
