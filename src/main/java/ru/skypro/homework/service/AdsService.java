package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.util.List;

/**
 * Интерфейс сервиса для работы с объявлениями.
 * Определяет основные операции, такие как создание, получение, обновление, удаление объявлений,
 * а также управление изображениями и получение объявлений текущего пользователя.
 */
public interface AdsService {

    Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, String username);

    void deleteAd(int id, String username);

    /**
     * Возвращает список всех объявлений.
     *
     * @return объект {@link Ads}, содержащий список всех объявлений и их количество.
     */
    Ads getAllAds();

    /**
     * Создает новое объявление.
     *
     * @param createOrUpdateAd объект {@link CreateOrUpdateAd}, содержащий данные для создания объявления.
     * @param image изображение, прикрепленное к объявлению.
     * @return объект {@link Ad}, представляющий созданное объявление.
     */
    Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image);

    /**
     * Возвращает подробную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return объект {@link ExtendedAd}, содержащий подробную информацию о объявлении.
     */
    ExtendedAd getAd(int id);

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления для удаления.
     */
    void deleteAd(int id);

    /**
     * Обновляет данные объявления по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @param updateAd объект {@link CreateOrUpdateAd}, содержащий обновленные данные объявления.
     * @return объект {@link Ad}, представляющий обновленное объявление.
     */
    Ad updateAd(int id, CreateOrUpdateAd updateAd);

    /**
     * Возвращает список объявлений текущего авторизованного пользователя.
     *
     * @return объект {@link Ads}, содержащий объявления авторизованного пользователя.
     */
    Ads getAdsByUser();

    /**
     * Обновляет изображение объявления по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @param image новое изображение для объявления.
     */
    void updateAdImage(int id, MultipartFile image);
}
