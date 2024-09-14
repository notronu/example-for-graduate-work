package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.impl.AdsServiceImpl;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdsController {
    private final AdsServiceImpl adsService;

    public AdsController(AdsServiceImpl adsService) {
        this.adsService = adsService;
    }

    @Operation(summary = "Получение всех объявлений", description = "", tags = {"Объявления"})
    @GetMapping
    public ResponseEntity<Ads> getAds() {
        return ResponseEntity.ok().body(new Ads());
    }

    @Operation(summary = "Добавление объявления", tags = {"Объявления"})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Ad> createAd(@RequestPart(value = "properties") CreateOrUpdateAd properties,
                                       @RequestPart(value = "image") MultipartFile image) {
        return ResponseEntity.ok().body(new Ad());
    }

    @Operation(summary = "Получение информации об объявлении", tags = {"Объявления"})
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdsExtended(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(new ExtendedAd());
    }

    @Operation(summary = "Удаление объявления", tags = {"Объявления"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAds(@PathVariable("id") int id) {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление информации об объявлении", tags = {"Объявления"})
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAds(@PathVariable("id") int id, @RequestBody CreateOrUpdateAd createOrUpdateAd) {
        return ResponseEntity.ok().body(new Ad());
    }

    @Operation(summary = "Получение объявлений авторизованного пользователя", tags = {"Объявления"})
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsAuthorizedUser() {
        return ResponseEntity.ok().body(new Ads());
    }

    @Operation(summary = "Обновление картинки объявления", tags = {"Объявления"})
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> updateImage(@PathVariable int id,
                                                    @RequestPart(value = "image") MultipartFile image) {
        return ResponseEntity.ok().body(new ArrayList<String>());
    }
}
