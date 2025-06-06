package ru.shift.models;

import ru.shift.exception.InvalidInputException;

import java.util.Map;

public class Rectangle implements Shape {
    private final double sideA;
    private final double sideB;

    public Rectangle(double sideA, double sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
    }

    @Override
    public String getName() {
        return "Прямоугольник";
    }

    @Override
    public double getArea() {
        return sideB * sideA;
    }

    @Override
    public double getPerimeter() {
        return (sideB + sideA) * 2;
    }

    @Override
    public Map<String, Double> getProperties() {
        return Map.of("Диагональ", getDiagonal(), "Длина", getHeight(),
                "Ширина", getWidth());
    }

    private double getDiagonal() {
        return Math.sqrt(sideB * sideB + sideA * sideA);
    }

    private double getWidth() {
        return Math.min(sideA, sideB);
    }

    private double getHeight() {
        return Math.max(sideA, sideB);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ShapeBuilder<Rectangle> {

        private double sideA;
        private double sideB;

        public Builder setSideA(double sideA) {
            this.sideA = sideA;
            return this;
        }

        public Builder setSideB(double sideB) {
            this.sideB = sideB;
            return this;
        }

        @Override
        public Rectangle build() throws InvalidInputException {
            if (sideA <= 0 || sideB <= 0) {
                throw new InvalidInputException("Аргументы должны быть положительными числами");
            }
            return new Rectangle(sideA, sideB);
        }
    }
}
