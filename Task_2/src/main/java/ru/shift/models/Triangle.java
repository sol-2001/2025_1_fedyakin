package ru.shift.models;

import ru.shift.exception.InvalidInputException;

import java.util.Map;

public class Triangle implements Shape {
    private final double sideA;
    private final double sideB;
    private final double sideC;

    public Triangle(double sideA, double sideB, double sideC) {
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    @Override
    public String getName() {
        return "Треугольник";
    }

    @Override
    public double getArea() {
        double p = getPerimeter()/2;
        return Math.sqrt(p * (p - sideA) * (p - sideB) * (p - sideC));
    }

    @Override
    public double getPerimeter() {
        return sideA + sideB + sideC;
    }

    @Override
    public Map<String, Double> getProperties() {
        return Map.of("Сторона А", sideA, "Сторона B", sideB,
                "Сторона C", sideC, "Угол напротив стороны А", getAngleOppositeSideA(),
                "Угол напротив стороны B", getAngleOppositeSideB(),
                "Угол напротив стороны C", getAngleOppositeSideC());
    }

    public double getAngleOppositeSideA() {
        return Math.toDegrees(Math.acos(((sideB * sideB) + (sideC * sideC) - (sideA * sideA)) / (2 * sideB * sideC)));
    }

    public double getAngleOppositeSideB() {
        return Math.toDegrees(Math.acos(((sideA * sideA) + (sideC * sideC) - (sideB * sideB)) / (2 * sideA * sideC)));
    }

    public double getAngleOppositeSideC() {
        return Math.toDegrees(Math.acos(((sideB * sideB) + (sideA * sideA) - (sideC * sideC)) / (2 * sideB * sideA)));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements ShapeBuilder<Triangle> {
        private double sideA;
        private double sideB;
        private double sideC;

        public Builder setSideA(double sideA) {
            this.sideA = sideA;
            return this;
        }

        public Builder setSideB(double sideB) {
            this.sideB = sideB;
            return this;
        }

        public Builder setSideC(double sideC) {
            this.sideC = sideC;
            return this;
        }

        @Override
        public Triangle build() throws InvalidInputException {
            if (sideA <= 0 || sideB <= 0 || sideC <= 0) {
                throw new InvalidInputException("Аргументы должны быть положительными числами");
            } else if (!isValidTriangle(sideA, sideB, sideC)) {
                throw new InvalidInputException("Стороны не образуют треугольник");
            }
            return new Triangle(sideA, sideB, sideC);
        }

        private boolean isValidTriangle(double a, double b, double c) {
            return a + b > c && a + c > b && b + c > a;
        }
    }
}
