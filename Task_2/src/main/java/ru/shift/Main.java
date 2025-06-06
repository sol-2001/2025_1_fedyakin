package ru.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import ru.shift.exception.OutputException;
import ru.shift.models.Shape;
import ru.shift.exception.InvalidInputException;
import ru.shift.reader.ShapeFileReader;
import ru.shift.reader.TextShapeFileReader;

import picocli.CommandLine.Option;
import ru.shift.writer.ConsoleShapeResultWriter;
import ru.shift.writer.FileShapeResultWriter;
import ru.shift.writer.ShapeResultWriter;

import java.io.IOException;

public class Main implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    @Option(names = {"-f", "--file"}, description = "Путь к файлу", required = true)
    private String filePath;

    @Option(names = {"-o", "--output"}, description = "Вывод в файл или в консоль", defaultValue = "")
    private String outputPath;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        try {
            log.info("Запуск приложения. Чтение файла: {}", filePath);

            ShapeFileReader reader = new TextShapeFileReader();
            Shape shape;
            try {
                shape = reader.readFileWithShape(filePath);
                log.info("Фигура успешно считана: {}", shape.getName());
            }
            catch (InvalidInputException e) {
                log.error("Shape read from {} failed", filePath, e);
                System.out.println(e.getMessage());
                return;
            } catch (IOException e) {
                log.error("Shape read from {} failed", filePath, e);
                System.out.println("При чтении файла " + filePath + " произошла ошибка:" + e.getMessage());
                return;
            }

            ShapeResultWriter writer;
            if (!outputPath.isEmpty()) {
                writer = new FileShapeResultWriter(outputPath);
            } else {
                writer = new ConsoleShapeResultWriter();
            }

            try {
                writer.writeShape(shape);
            } catch (OutputException e) {
                log.error("Ошибка вывода результата: {}", e.getMessage());
                System.out.println("При записи результата произошла ошибка:" + e.getMessage());
            }
        }
        catch (Exception e) {
            log.error("Неизвестная ошибка: {}", e.getMessage(), e);
        }
    }
}