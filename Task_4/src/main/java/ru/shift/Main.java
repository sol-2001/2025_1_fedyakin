package ru.shift;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shift.reader.ConsoleNumberReader;
import ru.shift.service.ComputationalService;
import ru.shift.writer.ResultConsoleWriter;

import java.math.BigDecimal;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try  {
            ConsoleNumberReader reader = new ConsoleNumberReader();
            ComputationalService service = new ComputationalService();
            ResultConsoleWriter writer = new ResultConsoleWriter();

            var number = reader.readNumber();

            long t0 = System.currentTimeMillis();
            BigDecimal result = service.compute(number);
            long elapsedMs = System.currentTimeMillis() - t0;

            writer.printResults(number, result, elapsedMs);
        } catch (RuntimeException re) {
            log.error(re.getMessage(), re);
        } catch (Exception e) {
            log.error("Неизвестная ошибка.", e);
        }
    }
}