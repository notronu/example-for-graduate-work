package ru.skypro.homework.utils;

/**
 * Утилитарный класс для логирования имени текущего метода.
 * Предоставляет статический метод для получения имени вызывающего метода.
 */
public class MethodLog {

    /**
     * Возвращает имя метода, из которого был вызван этот метод.
     *
     * @return строка, содержащая имя вызывающего метода.
     */
    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
