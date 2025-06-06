package ru.shift.writer;

import ru.shift.exception.OutputException;
import ru.shift.models.Shape;
import java.io.PrintWriter;

public interface ShapeResultWriter {
    String UNIT_MM = "мм";
    String UNIT_MM2 = "кв. мм";

    void writeShape(Shape shape) throws OutputException;

    default void printShape(PrintWriter writer, Shape shape) {
        writer.println("Тип фигуры: " + shape.getName());
        writer.println("Площадь: " + formatValue(shape.getArea(),UNIT_MM2));
        writer.println("Периметр: " + formatValue(shape.getPerimeter(), UNIT_MM));

        shape.getProperties().forEach((key, value) ->
                writer.println(key + ": " + formatValue(value, UNIT_MM)));
    }

    private String formatValue(Double value, String unit) {
        return String.format("%.2f %s", value,unit);
    }
}