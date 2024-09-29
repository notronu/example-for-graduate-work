package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.utils.MapperUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    @Override
    public Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        AdEntity adEntity = new AdEntity();
        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setDescription(createOrUpdateAd.getDescription());
        adEntity.setPrice(createOrUpdateAd.getPrice());
        adEntity.setImage(image.getOriginalFilename());
        adEntity.setAuthor(userEntity);

        adsRepository.save(adEntity);
        return MapperUtils.toAdDto(adEntity);
    }

    @Override
    public Ad updateAd(int adId, CreateOrUpdateAd updateAd, String username) {
        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        // Проверка прав пользователя
        if (!adEntity.getAuthor().getUsername().equals(currentUser.getUsername()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Вы не можете редактировать это объявление");
        }

        adEntity.setTitle(updateAd.getTitle());
        adEntity.setDescription(updateAd.getDescription());
        adEntity.setPrice(updateAd.getPrice());
        adsRepository.save(adEntity);

        return MapperUtils.toAdDto(adEntity);
    }

    @Override
    public void deleteAd(int adId, String username) {
        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        // Проверка прав пользователя
        if (!adEntity.getAuthor().getUsername().equals(currentUser.getUsername()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Вы не можете удалить это объявление");
        }

        adsRepository.delete(adEntity);
    }

    @Override
    public Ad getAd(int id) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        return MapperUtils.toAdDto(adEntity);
    }

    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntities = adsRepository.findAll();
        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(adEntities.stream().map(MapperUtils::toAdDto).collect(Collectors.toList()));
        return ads;
    }

    @Override
    public Ads getAdsByUser(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        List<AdEntity> adEntities = adsRepository.findByAuthorId(userEntity.getId());
        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(adEntities.stream().map(MapperUtils::toAdDto).collect(Collectors.toList()));
        return ads;
    }

    @Override
    public void updateAdImage(int adId, MultipartFile image, String username) {
        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        // Проверка прав пользователя
        if (!adEntity.getAuthor().getUsername().equals(currentUser.getUsername()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Вы не можете обновить изображение этого объявления");
        }

        adEntity.setImage(image.getOriginalFilename());
        adsRepository.save(adEntity);
    }
}
