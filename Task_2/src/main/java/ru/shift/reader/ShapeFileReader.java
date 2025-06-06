package ru.shift.reader;

import ru.shift.models.Shape;
import ru.shift.exception.InvalidInputException;

import java.io.IOException;

public interface ShapeFileReader {
    Shape readFileWithShape(String filePath) throws IOException, InvalidInputException;
}
