package ru.shift.models;

import org.junit.jupiter.api.Test;
import ru.shift.exception.InvalidInputException;

import static org.junit.jupiter.api.Assertions.*;

public class CircleTest {

    @Test
    public void testCircleCreation() throws InvalidInputException {
        Circle circle = Circle.builder().setRadius(5.0).build();

        assertEquals("Круг", circle.getName());
        assertEquals(Math.PI * 25, circle.getArea(), 0.0001);
        assertEquals(2 * Math.PI * 5, circle.getPerimeter(), 0.0001);
        assertEquals(5.0, circle.getProperties().get("Радиус"), 0.0001);
        assertEquals(10.0, circle.getProperties().get("Диаметр"), 0.0001);
    }

    @Test
    public void testInvalidRadius() {
        assertThrows(InvalidInputException.class, () -> Circle.builder().setRadius(-1).build());
        assertThrows(InvalidInputException.class, () -> Circle.builder().setRadius(0).build());
    }
}