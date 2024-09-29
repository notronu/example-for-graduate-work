package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.utils.MethodLog;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    @Operation(
            tags = "Объявления",
            summary = "Получение всех объявлений"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))})
    })
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        log.info("Использован метод {}", MethodLog.getMethodName());
        return ResponseEntity.ok(adsService.getAllAds());
    }

    @Operation(
            tags = "Объявления",
            summary = "Создание нового объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> createAd(@RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                       @RequestPart("image") MultipartFile image,
                                       Principal principal) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        Ad ad = adsService.createAd(createOrUpdateAd, image, principal.getName());
        return ResponseEntity.status(201).body(ad);
    }

    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении по ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))}),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable int id) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        ExtendedAd ad = adsService.getAd(id);
        return ResponseEntity.ok(ad);
    }

    @Operation(
            tags = "Объявления",
            summary = "Обновление объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable int id,
                                       @RequestBody CreateOrUpdateAd updateAd,
                                       Principal principal) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        Ad updatedAd = adsService.updateAd(id, updateAd, principal.getName());
        return ResponseEntity.ok(updatedAd);
    }

    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable int id, Principal principal) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        adsService.deleteAd(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            tags = "Объявления",
            summary = "Обновление изображения объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateAdImage(@PathVariable int id,
                                              @RequestPart("image") MultipartFile image) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        adsService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }
}
