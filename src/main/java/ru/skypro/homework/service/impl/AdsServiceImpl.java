package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления объявлениями.
 * Предоставляет методы для создания, получения, обновления, удаления объявлений,
 * а также для работы с изображениями объявлений.
 */
@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;

    /**
     * Получает список всех объявлений.
     *
     * @return объект {@link Ads}, содержащий список всех объявлений и их количество.
     */
    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntities = adsRepository.findAll();
        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(adEntities.stream().map(MapperUtils::toAdDto).collect(Collectors.toList()));
        return ads;
    }

    /**
     * Создает новое объявление с изображением.
     *
     * @param createOrUpdateAd объект {@link CreateOrUpdateAd} с информацией об объявлении.
     * @param image файл изображения для объявления.
     * @return объект {@link Ad}, представляющий созданное объявление.
     */
    @Override
    public Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image) {
        AdEntity adEntity = MapperUtils.toAdEntity(createOrUpdateAd);
        adEntity.setImage(image.getOriginalFilename());
        adsRepository.save(adEntity);
        return MapperUtils.toAdDto(adEntity);
    }

    /**
     * Получает подробную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return объект {@link ExtendedAd}, содержащий полную информацию об объявлении,
     * или {@code null}, если объявление не найдено.
     */
    @Override
    public ExtendedAd getAd(int id) {
        Optional<AdEntity> adEntity = adsRepository.findById(id);
        return adEntity.map(MapperUtils::toExtendedAdDto).orElse(null);
    }

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления.
     */
    @Override
    public void deleteAd(int id) {
        adsRepository.deleteById(id);
    }

    /**
     * Обновляет существующее объявление.
     *
     * @param id идентификатор объявления.
     * @param updateAd объект {@link CreateOrUpdateAd}, содержащий обновленные данные объявления.
     * @return объект {@link Ad}, представляющий обновленное объявление,
     * или {@code null}, если объявление не найдено.
     */
    @Override
    public Ad updateAd(int id, CreateOrUpdateAd updateAd) {
        Optional<AdEntity> adEntityOptional = adsRepository.findById(id);
        if (adEntityOptional.isPresent()) {
            AdEntity adEntity = adEntityOptional.get();
            adEntity.setTitle(updateAd.getTitle());
            adEntity.setDescription(updateAd.getDescription());
            adEntity.setPrice(updateAd.getPrice());
            adsRepository.save(adEntity);
            return MapperUtils.toAdDto(adEntity);
        }
        return null;
    }

    /**
     * Получает объявления авторизованного пользователя.
     * В текущей реализации возвращает все объявления.
     *
     * @return объект {@link Ads}, содержащий список объявлений.
     */
    @Override
    public Ads getAdsByUser() {
        // Заглушка, пока нет авторизации
        return getAllAds();
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id идентификатор объявления.
     * @param image новый файл изображения для объявления.
     */
    @Override
    public void updateAdImage(int id, MultipartFile image) {
        Optional<AdEntity> adEntityOptional = adsRepository.findById(id);
        if (adEntityOptional.isPresent()) {
            AdEntity adEntity = adEntityOptional.get();
            adEntity.setImage(image.getOriginalFilename());
            adsRepository.save(adEntity);
        }
    }
}
