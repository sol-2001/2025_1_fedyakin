package ru.shift.writer;

import org.junit.jupiter.api.Test;
import ru.shift.exception.OutputException;
import ru.shift.models.Circle;
import ru.shift.models.Shape;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileShapeResultWriterTest {

    @Test
    public void testWriteShapeToFileValid() throws IOException {
        Shape circle = Circle.builder().setRadius(5.0).build();
        Path tempFile = Files.createTempFile("shapeResult", ".txt");
        String filePath = tempFile.toString();
        FileShapeResultWriter writer = new FileShapeResultWriter(filePath);
        writer.writeShape(circle);
        String content = Files.readString(tempFile);

        assertTrue(content.contains("Тип фигуры: Круг"));
        assertTrue(content.contains("Площадь:"));
        assertTrue(content.contains("Периметр:"));
        assertTrue(content.contains("Радиус: 5,00 мм"));
        assertTrue(content.contains("Диаметр: 10,00 мм"));

        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testWriteShapeToFileEmptyOutputPath() {
        Shape circle = Circle.builder().setRadius(5.0).build();
        FileShapeResultWriter writer = new FileShapeResultWriter("");
        Exception exception = assertThrows(OutputException.class, () -> writer.writeShape(circle));
        assertTrue(exception.getMessage().contains("Путь для вывода не может быть пустым"));
    }
}
