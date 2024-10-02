package ru.skypro.homework.utils;

import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdMapper {
    public Ad toAdDto(AdEntity adEntity) {
        Ad ad = new Ad();

        ad.setPk(adEntity.getId());
        ad.setAuthor(adEntity.getUser().getId());
        ad.setImage("/ads/" + adEntity.getId() + "/image");
        ad.setPrice((adEntity.getPrice()));
        ad.setTitle(adEntity.getTitle());

        return ad;
    }

    public Ads toAdsDto(List<AdEntity> ads1) {
        Ads ads = new Ads();
        List<Ad> adDtoList = ads1.stream()
                .map(this::toAdDto)
                .collect(Collectors.toList());

        ads.setCount(adDtoList.size());
        ads.setResults(adDtoList);

        return ads;
    }

    public AdEntity toEntity (CreateOrUpdateAd createOrUpdateAd) {
        AdEntity adEntity = new AdEntity();

        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setDescription(createOrUpdateAd.getDescription());
        adEntity.setPrice(createOrUpdateAd.getPrice());

        return adEntity;

    }

    public ExtendedAd toExtendedAdDto(AdEntity adEntity) {
        ExtendedAd extendedAd = new ExtendedAd();

        extendedAd.setPk(adEntity.getId());
        extendedAd.setAuthorFirstName(adEntity.getUser().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getUser().getLastName());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setEmail(adEntity.getUser().getEmail());
        extendedAd.setImage("/ads/" + adEntity.getId() + "/image");
        extendedAd.setPhone(adEntity.getUser().getPhone());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setTitle(adEntity.getTitle());

        return extendedAd;
    }
}
