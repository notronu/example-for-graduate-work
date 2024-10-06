package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;

/**
 * Интерфейс сервиса для обработки авторизации и регистрации пользователей.
 * Определяет основные операции для входа в систему и регистрации новых пользователей.
 */
public interface AuthService {

    /**
     * Выполняет авторизацию пользователя на основе его имени пользователя и пароля.
     *
     * @param userName имя пользователя для входа.
     * @param password пароль для входа.
     * @return {@code true}, если авторизация успешна, иначе {@code false}.
     */
    boolean login(String userName, String password);

    boolean login(Login login);

}
