package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

/**
 * Интерфейс сервиса для управления данными пользователей.
 * Определяет основные операции для изменения пароля, получения и обновления информации о пользователе, а также обновления аватара.
 */
public interface UserService {



    /**
     * Устанавливает новый пароль для пользователя.
     * Проверяет текущий пароль и обновляет его на новый, если проверка успешна.
     *
     * @param newPassword объект {@link NewPassword}, содержащий текущий и новый пароли.
     * @return {@code true}, если пароль был успешно изменен, иначе {@code false}.
     */
    boolean setNewPassword(NewPassword newPassword);

    /**
     * Возвращает информацию о текущем пользователе.
     *
     * @return объект {@link User}, представляющий данные о текущем пользователе.
     */
    User getUser();

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * @param updateUser объект {@link UpdateUser}, содержащий обновленные данные пользователя.
     * @return объект {@link UpdateUser}, представляющий обновленные данные пользователя.
     */
    UpdateUser updateUser(UpdateUser updateUser);



    /**
     *  Регистрирует нового пользователя.
     *
     *       @param dto Данные для регистрации пользователя.
     *       @return Зарегистрированный пользователь.
     *       @throws ru.skypro.homework.exception.UserAlreadyRegisteredException Если пользователь с таким email уже существует.
     */
    UserEntity registerUser(Register dto);

    /**
     *  Возвращает изображение пользователя по его имени.
     *
     *       @param username Имя пользователя.
     *       @return Массив байтов, представляющий изображение.
     *       @throws UsernameNotFoundException Если пользователь с данным именем не найден.
     */
    byte[] getImage(String username);

    /**
     * Обновляет изображение текущего пользователя.
     *
     *       @param username Имя пользователя.
     *       @param file     Файл изображения.
     *       @throws IOException             Если произошла ошибка при чтении файла или записи изображения.
     *       @throws UsernameNotFoundException Если пользователь с данным именем не найден.
     */
    void updateMyImage(String username, MultipartFile file) throws IOException;
}
