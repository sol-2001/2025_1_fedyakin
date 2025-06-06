package ru.shift.reader;

import ru.shift.models.Shape;
import ru.shift.ShapeFactory;
import ru.shift.exception.InvalidInputException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class TextShapeFileReader implements ShapeFileReader {

    @Override
    public Shape readFileWithShape(String filePath) throws IOException, InvalidInputException {

        if (filePath == null || filePath.trim().isEmpty()) {
            throw new InvalidInputException("Путь к файлу не может быть пустым");
        }

        if (!Files.exists(Paths.get(filePath))) {
            throw new InvalidInputException("Файл не существует: " + filePath);
        }

        try (var reader = new BufferedReader(new FileReader(filePath))) {
            String shapeName = readNotEmptyLine(reader);
            String paramsLine = readNotEmptyLine(reader);

            if (reader.readLine() != null) {
                throw new InvalidInputException("Неверный формат файла: много строк");
            }

            List<Double> params = Arrays.stream(paramsLine.split("\\s+"))
                    .map(s -> {
                        try {
                            return Double.parseDouble(s);
                        } catch (NumberFormatException e) {
                            throw new InvalidInputException("Неверный формат чисел в параметрах: " + s);
                        }
                    }).toList();

            return ShapeFactory.createShape(shapeName, params);
        }
    }

    private String readNotEmptyLine(BufferedReader reader) throws IOException, InvalidInputException {
        var line = reader.readLine();
        if (line == null) {
            throw new InvalidInputException("Ожидалась строка, но файл закончился");
        }

        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            throw new InvalidInputException("Пустая строка");
        }

        return trimmed;
    }
}
