package ru.skypro.homework.exception;

/**
 * Исключение, которое вызывается, когда текущий пароль пользователя неверен.
 * <p>
 * Это исключение наследуется от {@link RuntimeException}, что позволяет
 * использовать его для обработки ошибок, связанных с неправильным паролем,
 * во время выполнения программы.
 *
 * @see RuntimeException
 */
public class PasswordIsNotCorrectException extends RuntimeException {

    /**
     * Переопределяет метод {@link RuntimeException#getMessage()} для предоставления
     * конкретного сообщения об ошибке.
     *
     * @return Строка с сообщением о том, что текущий пароль неверен.
     */
    @Override
    public String getMessage() {
        return "Current password is not correct :(";
    }
}
