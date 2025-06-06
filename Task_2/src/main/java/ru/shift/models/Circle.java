package ru.shift.models;

import ru.shift.exception.InvalidInputException;

import java.util.Map;

public class Circle implements Shape {

    private final double radius;

    private Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public String getName() {
        return "Круг";
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public Map<String, Double> getProperties() {
        return Map.of("Радиус", radius, "Диаметр", getDiameter());
    }

    private double getDiameter() {
        return 2 * radius;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ShapeBuilder<Circle> {
        private double radius;

        public Builder setRadius(double radius) {
            this.radius = radius;
            return this;
        }

        @Override
        public Circle build() throws InvalidInputException {
            if (radius <= 0) {
                throw new InvalidInputException("Аргумент должен быть положительным числом");
            }
            return new Circle(radius);
        }
    }
}
