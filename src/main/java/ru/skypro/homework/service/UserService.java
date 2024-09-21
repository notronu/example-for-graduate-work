package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

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
     * Обновляет аватар текущего пользователя.
     *
     * @param image новое изображение профиля (аватар), прикрепленное к запросу.
     */
    void updateImage(MultipartFile image);
}
