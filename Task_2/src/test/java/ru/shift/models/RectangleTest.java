package ru.shift.models;

import org.junit.jupiter.api.Test;
import ru.shift.exception.InvalidInputException;

import static org.junit.jupiter.api.Assertions.*;

public class RectangleTest {

    @Test
    public void testRectangleCreation() throws InvalidInputException {
        Rectangle rectangle = Rectangle.builder().setSideA(3.0).setSideB(4.0).build();

        assertEquals("Прямоугольник", rectangle.getName());
        assertEquals(12.0, rectangle.getArea());
        assertEquals(14.0, rectangle.getPerimeter());
        assertEquals(5.0, rectangle.getProperties().get("Диагональ"), 0.0001);
        assertEquals(4.0, rectangle.getProperties().get("Длина"));
        assertEquals(3.0, rectangle.getProperties().get("Ширина"));
    }

    @Test
    public void testInvalidSides() {
        assertThrows(InvalidInputException.class, () -> Rectangle.builder().setSideA(-1).setSideB(2).build());
        assertThrows(InvalidInputException.class, () -> Rectangle.builder().setSideA(0).setSideB(2).build());
        assertThrows(InvalidInputException.class, () -> Rectangle.builder().setSideA(1).setSideB(-2).build());
    }
}