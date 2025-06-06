package ru.shift.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.exception.OutputException;
import ru.shift.models.Shape;

import java.io.IOException;
import java.io.PrintWriter;

public class ConsoleShapeResultWriter implements ShapeResultWriter {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleShapeResultWriter.class);

    @Override
    public void writeShape(Shape shape) throws OutputException {
        logger.debug("Вывод фигуры {} в консоль", shape.getName());
        try (PrintWriter writer = new PrintWriter(System.out)) {
            printShape(writer, shape);
            writer.flush();
            // Нужно ли делать проверку для System.out?
            if (writer.checkError()) {
                throw new OutputException("Ошибка вывода фигуры в консоль"); //?
            }
            logger.info("Успешный вывод фигуры {} в консоль", shape.getName());
        } catch (IOException e) {
            throw new OutputException("Неизвестная ошибка при выводе в консоль", e);
        }
    }
}
