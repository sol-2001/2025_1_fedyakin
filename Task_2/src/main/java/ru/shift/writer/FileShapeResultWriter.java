package ru.shift.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.exception.OutputException;
import ru.shift.models.Shape;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileShapeResultWriter implements ShapeResultWriter {
    private static final Logger logger = LoggerFactory.getLogger(FileShapeResultWriter.class);
    private final String outputPath;

    public FileShapeResultWriter(String outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    public void writeShape(Shape shape) throws OutputException {
        if (outputPath == null || outputPath.trim().isEmpty()) {
            throw new OutputException("Путь для вывода не может быть пустым");
        }

        try {
            Path parent = Paths.get(outputPath).getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new OutputException("Ошибка создания директории: " + outputPath, e);
        }

        logger.debug("Запись фигуры {} в файл: {}", shape.getName(), outputPath);


        try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
            printShape(writer, shape);
            logger.debug("Фигура успешно записана в файл {}", outputPath);

            writer.flush();
            if (writer.checkError()) {
                throw new OutputException("Неизвестная ошибка записи фигуры в файл " + outputPath);
            }
        } catch (IOException e) {
            throw new OutputException("Ошибка записи фигуры в файл: " + outputPath, e);
        }
    }
}
