package ru.shift;

import ru.shift.exception.InvalidInputException;
import ru.shift.models.Circle;
import ru.shift.models.Rectangle;
import ru.shift.models.Shape;
import ru.shift.models.Triangle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeFactory {
    @FunctionalInterface
    interface ShapeCreator {
        Shape create(List<Double> params) throws InvalidInputException;
    }

    private static final Map<String, ShapeCreator> registry = new HashMap<>();

    static {
        registry.put("CIRCLE", params -> {
            if (params.size() != 1) {
                throw new InvalidInputException("Для CIRCLE требуется 1 параметр: radius");
            }
            return Circle.builder()
                    .setRadius(params.get(0))
                    .build();
        });

        registry.put("RECTANGLE", params -> {
            if (params.size() != 2) {
                throw new InvalidInputException("Для RECTANGLE требуется 2 параметра: height, width");
            }
            return Rectangle.builder()
                    .setSideA(params.get(0))
                    .setSideB(params.get(1))
                    .build();
        });

        registry.put("TRIANGLE", params -> {
            if (params.size() != 3) {
                throw new InvalidInputException("Для TRIANGLE требуется 3 параметра");
            }
            return Triangle.builder()
                    .setSideA(params.get(0))
                    .setSideB(params.get(1))
                    .setSideC(params.get(2))
                    .build();
        });
    }

    public static Shape createShape(String shapeType, List<Double> params) throws InvalidInputException {
        shapeType = shapeType.toUpperCase();
        ShapeCreator creator = registry.get(shapeType);
        if (creator == null) {
            throw new InvalidInputException("Неизвестный тип фигуры: " + shapeType);
        }
        return creator.create(params);
    }
}
