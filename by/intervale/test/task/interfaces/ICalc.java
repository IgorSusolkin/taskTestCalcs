package by.intervale.test.task.interfaces;
/**
 * Интерфейс калькурятора
 * @author Igor Susolkin
 *
 */
public interface ICalc {
    /**
     *
     * @param line - входная строка
     * @return результат или ошибку в текстовром виде
     */
    String exec(String line);
}
