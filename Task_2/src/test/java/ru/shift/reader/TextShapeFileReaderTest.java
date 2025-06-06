package ru.shift.reader;

import org.junit.jupiter.api.Test;
import ru.shift.exception.InvalidInputException;
import ru.shift.models.Shape;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TextShapeFileReaderTest {

    @Test
    public void testNullFilePath() {
        TextShapeFileReader reader = new TextShapeFileReader();

        Exception exception = assertThrows(InvalidInputException.class, () -> reader.readFileWithShape(null));
        assertTrue(exception.getMessage().contains("не может быть пустым"));
    }

    @Test
    public void testNonExistentFile() {
        TextShapeFileReader reader = new TextShapeFileReader();
        String fakePath = "non_existent_file.txt";

        Exception exception = assertThrows(InvalidInputException.class, () -> reader.readFileWithShape(fakePath));
        assertTrue(exception.getMessage().contains("Файл не существует"));
    }

    @Test
    public void testEmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("empty", ".txt");
        TextShapeFileReader reader = new TextShapeFileReader();

        Exception exception = assertThrows(InvalidInputException.class, () -> reader.readFileWithShape(tempFile.toString()));
        assertTrue(exception.getMessage().contains("Ожидалась строка"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testFileWithOneLine() throws IOException {
        Path tempFile = Files.createTempFile("oneLine", ".txt");
        Files.writeString(tempFile, "CIRCLE");
        TextShapeFileReader reader = new TextShapeFileReader();

        Exception exception = assertThrows(InvalidInputException.class, () -> reader.readFileWithShape(tempFile.toString()));

        assertTrue(exception.getMessage().contains("Ожидалась строка"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testFileWithMoreThanTwoLines() throws IOException {
        Path tempFile = Files.createTempFile("moreLines", ".txt");
        Files.writeString(tempFile, "CIRCLE\n5.0\nExtra line");
        TextShapeFileReader reader = new TextShapeFileReader();

        Exception exception = assertThrows(InvalidInputException.class, () -> reader.readFileWithShape(tempFile.toString()));
        assertTrue(exception.getMessage().contains("Неверный формат файла: много строк"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testInvalidNumberFormat() throws IOException {
        Path tempFile = Files.createTempFile("invalidNumber", ".txt");
        Files.writeString(tempFile, "CIRCLE\nnot_a_number");
        TextShapeFileReader reader = new TextShapeFileReader();

        Exception exception = assertThrows(InvalidInputException.class, () -> reader.readFileWithShape(tempFile.toString()));
        assertTrue(exception.getMessage().contains("Неверный формат чисел"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testValidCircleFile() throws IOException, InvalidInputException {
        Path tempFile = Files.createTempFile("circle", ".txt");
        Files.writeString(tempFile, "CIRCLE\n5.0");
        TextShapeFileReader reader = new TextShapeFileReader();
        Shape shape = reader.readFileWithShape(tempFile.toString());

        assertNotNull(shape);
        assertEquals("Круг", shape.getName());

        Files.deleteIfExists(tempFile);
    }
}
