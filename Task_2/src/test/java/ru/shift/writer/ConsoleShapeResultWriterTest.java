package ru.shift.writer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.shift.exception.OutputException;
import ru.shift.models.Circle;
import ru.shift.models.Shape;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ConsoleShapeResultWriterTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outContent, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testWriteShapeToConsole() throws OutputException {
        Shape circle = Circle.builder().setRadius(5.0).build();
        ConsoleShapeResultWriter writer = new ConsoleShapeResultWriter();
        writer.writeShape(circle);
        String output = outContent.toString();

        assertTrue(output.contains("Тип фигуры: Круг"));
        assertTrue(output.contains("Площадь:"));
        assertTrue(output.contains("Периметр:"));
        assertTrue(output.contains("Радиус: 5,00 мм"));
        assertTrue(output.contains("Диаметр: 10,00 мм"));
    }
}
