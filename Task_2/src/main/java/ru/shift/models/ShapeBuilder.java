package ru.shift.models;

import ru.shift.exception.InvalidInputException;

public interface ShapeBuilder<T extends Shape> {
    T build() throws InvalidInputException;
}
