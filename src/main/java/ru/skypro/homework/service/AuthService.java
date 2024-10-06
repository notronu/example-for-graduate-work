package ru.skypro.homework.service;

import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;

/**
 * Интерфейс сервиса для обработки авторизации и регистрации пользователей.
 * Определяет основные операции для входа в систему и регистрации новых пользователей.
 */
public interface AuthService {

    /**
     * Проверяет валидность логина пользователя.
     *
     * @param userName Имя пользователя.
     * @param password Пароль пользователя.
     * @return true, если логин и пароль верны, false - иначе.
     */
    boolean login(String userName, String password);

    /**
     * Проверяет валидность логина пользователя.
     *
     * @param login Объект, содержащий имя пользователя и пароль.
     * @return true, если логин и пароль верны, false - иначе.
     */
    boolean login(Login login);

}
