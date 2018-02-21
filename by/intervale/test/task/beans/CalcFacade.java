package by.intervale.test.task.beans;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import by.intervale.test.task.interfaces.ICalc;
/**
 * Реализует универсальный интерфейс для чтения исходных выражений из файла.
 * Вызова соответствующуго калькулятора и записи результата в соот файл.
 * @author Igor Susolkin
 *
 */
public class CalcFacade {
    public void exec(final String inSourse, final String outSourse, final ICalc calc) {
        Scanner sc = null;
        FileWriter writer = null;
        try {
            sc = new Scanner(new FileReader(inSourse));
            writer = new FileWriter(outSourse);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String result = calc.exec(line);
                writer.append(line).append(" = ");
                writer.append(result).append("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found");
        } catch (IOException e) {
            System.err.println("Output file can't be created");
        } finally {
            close(writer);
            close(sc);
        }
    }

    private void close(final Closeable c) {
        if (c == null) {
            return;
        }
        try {
            c.close();
        } catch (IOException e) {
            System.err.println("IOException" + c);
        }
     }
}
