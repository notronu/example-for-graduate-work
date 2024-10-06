package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.utils.AdMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


@Service
@RequiredArgsConstructor
public class AdsServiceImpl implements AdsService {

    @Value("${path.to.ad.photo}")
    private String photoPath;
    private final AdMapper adMapper;
    private final AdsRepository adsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public Ad addAd(CreateOrUpdateAd createOrUpdateAd, MultipartFile image, Authentication authentication) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));
        AdEntity adEntity = adMapper.toEntity(createOrUpdateAd);
        adEntity.setUser(userEntity);
        adEntity = adsRepository.save(adEntity);
        return adMapper.toAdDto(adsRepository.save(uploadImage(adEntity, image)));
    }

    private AdEntity uploadImage(AdEntity adEntity, MultipartFile image) throws IOException {
        Path filePath = Path.of(photoPath, adEntity.hashCode() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = image.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
            adEntity.setImage(filePath.toString());
            return adsRepository.save(adEntity);
        }

    }

    @Override
    public Ads getAll() {
        return adMapper.toAdsDto(adsRepository.findAll());
    }

    @Override
    public Ads getMyAds(String username) {
        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return adMapper.toAdsDto(adsRepository.findAllByUserId(userEntity.getId()));
    }

    @Override
    public AdEntity getAd(Integer id) {
        return adsRepository.findById(id).orElseThrow(() -> new AdNotFoundException(id));
    }

    @Override
    public byte[] getImage(Integer id) throws IOException {
        AdEntity adEntity = getAd(id);
        return Files.readAllBytes(Path.of(adEntity.getImage()));
    }

    @Override
    public ExtendedAd getExtendedAd(Integer id) {
        return adMapper.toExtendedAdDto(getAd(id));
    }

    @Override
    public Ad updateAd(Integer id, CreateOrUpdateAd createOrUpdateAd, Authentication authentication) {
        AdEntity adEntity = getAd(id);
        adEntity.setDescription(createOrUpdateAd.getDescription());
        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setPrice(createOrUpdateAd.getPrice());
        return adMapper.toAdDto(adsRepository.save(adEntity));
    }

    @Override
    public byte[] updateAdImage(Integer id, MultipartFile image, Authentication authentication) throws IOException {

        AdEntity ad = getAd(id);
        ad = uploadImage(ad, image);
        return Files.readAllBytes(Path.of(ad.getImage()));


    }

    @Override
    public void deleteAd(Integer id, Authentication authentication) throws AdNotFoundException {
        if (adsRepository.existsById(id)) {

            List<CommentEntity> commentEntity = commentRepository.findByAdId(id);
            for (CommentEntity c : commentEntity)
                commentRepository.delete(c);

            adsRepository.delete(getAd(id));
        } else {
            throw new AdNotFoundException(id);
        }
    }

}
