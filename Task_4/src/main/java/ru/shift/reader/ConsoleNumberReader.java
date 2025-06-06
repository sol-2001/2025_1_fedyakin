package ru.shift.reader;

import java.util.Scanner;

// Исходя из предположения, что дальше возможно расширение и чтение числа из файла (например),
// то можно будет создать интерфейс (какой нибудь NumberReader) и заимплиментить этот класс от него.
// Поэтому выбрал такое имя класса и метода
public class ConsoleNumberReader {
    private final Scanner scanner;

    public ConsoleNumberReader() {
        this.scanner = new Scanner(System.in);
    }

    public long readNumber() {
        long number;
        while (true) {
            System.out.print("Введите N (≥ 1): ");
            if (!scanner.hasNextLong()) {
                System.out.println("Это не число. Попробуйте ещё раз.");
                scanner.next();
                continue;
            }

            number = scanner.nextLong();
            if (number < 1) {
                System.out.println("Число должно быть больше либо равно 1. Введено: " + number);
            } else {
                return number;
            }
        }
    }
}
