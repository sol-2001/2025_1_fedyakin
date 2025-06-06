package ru.shift.models;

import org.junit.jupiter.api.Test;
import ru.shift.exception.InvalidInputException;

import static org.junit.jupiter.api.Assertions.*;

public class TriangleTest {

    @Test
    public void testTriangleCreation() throws InvalidInputException {
        Triangle triangle = Triangle.builder().setSideA(3).setSideB(4).setSideC(5).build();
        assertEquals("Треугольник", triangle.getName());
        assertEquals(6.0, triangle.getArea(), 0.0001);
        assertEquals(12.0, triangle.getPerimeter());
        assertEquals(3.0, triangle.getProperties().get("Сторона А"));
        assertEquals(4.0, triangle.getProperties().get("Сторона B"));
        assertEquals(5.0, triangle.getProperties().get("Сторона C"));
        assertEquals(90, triangle.getProperties().get("Угол напротив стороны C"), 0.0001);
    }

    @Test
    public void testInvalidSides() {
        assertThrows(InvalidInputException.class, () -> Triangle.builder().setSideA(1).setSideB(1).setSideC(3).build());
        assertThrows(InvalidInputException.class, () -> Triangle.builder().setSideA(0).setSideB(2).setSideC(3).build());
        assertThrows(InvalidInputException.class, () -> Triangle.builder().setSideA(-1).setSideB(2).setSideC(3).build());
    }
}