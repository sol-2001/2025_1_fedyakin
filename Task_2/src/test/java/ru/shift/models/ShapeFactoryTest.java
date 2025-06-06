package ru.shift.models;

import org.junit.jupiter.api.Test;
import ru.shift.ShapeFactory;
import ru.shift.exception.InvalidInputException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShapeFactoryTest {

    @Test
    public void testCreateCircle() throws InvalidInputException {
        Shape shape = ShapeFactory.createShape("CIRCLE", List.of(5.0));
        assertNotNull(shape);
        assertInstanceOf(Circle.class, shape);
        assertEquals("Круг", shape.getName());
    }

    @Test
    public void testCreateRectangle() throws InvalidInputException {
        Shape shape = ShapeFactory.createShape("RECTANGLE", List.of(3.0, 4.0));
        assertNotNull(shape);
        assertInstanceOf(Rectangle.class, shape);
        assertEquals("Прямоугольник", shape.getName());
    }

    @Test
    public void testCreateTriangle() throws InvalidInputException {
        Shape shape = ShapeFactory.createShape("TRIANGLE", List.of(3.0, 4.0, 5.0));
        assertNotNull(shape);
        assertInstanceOf(Triangle.class, shape);
        assertEquals("Треугольник", shape.getName());
    }

    @Test
    public void testUnknownShapeType() {
        Exception exception = assertThrows(InvalidInputException.class, () ->
                ShapeFactory.createShape("PENTAGON", List.of(1.0, 2.0, 3.0, 4.0, 5.0))
        );
        assertTrue(exception.getMessage().contains("Неизвестный тип фигуры"));
    }
}
