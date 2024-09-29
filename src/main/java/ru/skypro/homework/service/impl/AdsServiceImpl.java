package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.utils.MapperUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public abstract class AdsServiceImpl implements AdsService {

    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    @Override
    public Ad createAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, String username) {
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByUsername(username));
        UserEntity userEntity = userEntityOptional.orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

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

        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByUsername(username));
        UserEntity currentUser = userEntityOptional.orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

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

        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByUsername(username));
        UserEntity currentUser = userEntityOptional.orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        // Проверка прав пользователя
        if (!adEntity.getAuthor().getUsername().equals(currentUser.getUsername()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Вы не можете удалить это объявление");
        }

        adsRepository.delete(adEntity);
    }

    @Override
    public ExtendedAd getAd(int id) {
        AdEntity adEntity = adsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        return MapperUtils.toExtendedAdDto(adEntity);
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
        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByUsername(username));
        UserEntity userEntity = userEntityOptional.orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        List<AdEntity> adEntities = adsRepository.findByAuthorId(userEntity.getId());
        Ads ads = new Ads();
        ads.setCount(adEntities.size());
        ads.setResults(adEntities.stream().map(MapperUtils::toAdDto).collect(Collectors.toList()));
        return ads;
    }

    @Override
    public void updateAdImage(int adId, MultipartFile image, String username) throws IOException {
        AdEntity adEntity = adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено"));

        Optional<UserEntity> userEntityOptional = Optional.ofNullable(userRepository.findByUsername(username));
        UserEntity currentUser = userEntityOptional.orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        // Проверка прав пользователя
        if (!adEntity.getAuthor().getUsername().equals(currentUser.getUsername()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Вы не можете обновить изображение этого объявления");
        }

        adEntity.setImage(image.getOriginalFilename());
        adsRepository.save(adEntity);
    }
}
