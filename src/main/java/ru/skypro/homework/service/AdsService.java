package ru.skypro.homework.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import org.springframework.security.core.Authentication;
import ru.skypro.homework.entity.AdEntity;

import java.io.IOException;

/**
 * Интерфейс сервиса для работы с объявлениями.
 * Определяет основные операции, такие как создание, получение, обновление, удаление объявлений,
 * а также управление изображениями и получение объявлений текущего пользователя.
 */
@Service
public interface AdsService {

    /**
     * /* Добавляет новое объявление.
     *
     * @param createOrUpdateAd DTO с информацией о новом объявлении.
     * @param image            Изображение объявления.
     * @param authentication   Аутентификация пользователя.
     * @return DTO с информацией о созданном объявлении.
     * @throws IOException Если произошла ошибка при загрузке изображения.
     */
    Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * Возвращает список всех объявлений.
     *
     * @return DTO со списком всех объявлений.
     */
    Ads getAll();

    /**
     * Возвращает список объявлений пользователя.
     *
     * @param username Имя пользователя.
     * @return DTO со списком объявлений пользователя.
     */
    Ads getMyAds(String username);

    /**
     * Возвращает объявление по ID.
     *
     * @param id ID объявления.
     * @return Объявление.
     */
    AdEntity getAd(Integer id);

    /**
     * Возвращает изображение объявления.
     *
     * @param id ID объявления.
     * @return Изображение объявления.
     * @throws IOException Если произошла ошибка при чтении изображения.
     */
    byte[] getImage(Integer id) throws IOException;

    /**
     * Возвращает подробную информацию об объявлении.
     *
     * @param id ID объявления.
     * @return DTO с подробной информацией об объявлении.
     */
    ExtendedAd getExtendedAd(Integer id);

    /**
     * Обновляет объявление.
     *
     * @param id                  Идентификатор объявления.
     * @param createOrUpdateAdDto Данные для обновления объявления.
     * @param authentication      Аутентификация пользователя.
     * @return Обновленное объявление в виде DTO.
     */
    Ad updateAd(Integer id, CreateOrUpdateAd createOrUpdateAdDto, Authentication authentication);

    /**
     * Обновляет изображение объявления.
     *
     * @param id             Идентификатор объявления.
     * @param image          Новый файл изображения.
     * @param authentication Аутентификация пользователя.
     * @return Бинарные данные обновленного изображения.
     * @throws IOException Ошибка ввода-вывода.
     */
    byte[] updateAdImage(Integer id, MultipartFile image, Authentication authentication) throws IOException;

    /**
     * Удаляет объявление.
     *
     * @param id             Идентификатор объявления.
     * @param authentication Аутентификация пользователя.
     * @throws ru.skypro.homework.exception.AdNotFoundException Исключение, если объявление не найдено.
     */
    void deleteAd(Integer id, Authentication authentication);
}