package ru.shift.models;

import java.util.Map;

public interface Shape {
    String getName();
    double getArea();
    double getPerimeter();
    Map<String, Double> getProperties();
}
