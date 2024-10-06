package ru.skypro.homework.service;

import ru.skypro.homework.dto.Register;

public interface RegistrationService {

    /**
     * Регистрирует нового пользователя.
     *
     * @param register Данные для регистрации нового пользователя.
     * @return true, если регистрация прошла успешно, иначе false.
     */
    boolean register(Register register);
}
