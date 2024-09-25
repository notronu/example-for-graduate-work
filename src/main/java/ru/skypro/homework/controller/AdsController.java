package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.utils.MethodLog;

/**
 * Контроллер для обработки операций с объявлениями.
 * Предоставляет эндпоинты для создания, получения, обновления и удаления объявлений,
 * а также для работы с изображениями и получения объявлений пользователя.
 */
@Slf4j
@RestController
@Tag(name = "Объявления")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class AdsController {
    private final AdsService adsService;

    /**
     * Эндпоинт для получения всех объявлений.
     *
     * @return Ответ, содержащий список всех объявлений.
     */
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
        Ads ads = adsService.getAllAds();
        return ResponseEntity.ok(ads);
    }

    /**
     * Эндпоинт для добавления нового объявления.
     *
     * @param createOrUpdateAd Данные для создания или обновления объявления.
     * @param image            Изображение, прикрепленное к объявлению.
     * @return Ответ, содержащий созданное объявление.
     */
    @Operation(
            tags = "Объявления",
            summary = "Добавление объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Ad> createAd(@RequestPart("properties") CreateOrUpdateAd createOrUpdateAd,
                                       @RequestPart("image") MultipartFile image) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        Ad ad = adsService.createAd(createOrUpdateAd, image);
        return ResponseEntity.status(201).body(ad);
    }

    /**
     * * Эндпоинт для получения информации об объявлении по его идентификатору.
     * *
     *
     * @param id Идентификатор объявления.
     * @return Ответ, содержащий подробную информацию об объявлении.
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение информации об объявлении"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))}),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable int id) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        ExtendedAd ad = adsService.getAd(id);
        return ResponseEntity.ok(ad);
    }

    /**
     * Эндпоинт для удаления объявления по его идентификатору.
     *
     * @param id Идентификатор объявления.
     * @return Ответ с кодом состояния 204, если удаление выполнено успешно.
     */
    @Operation(
            tags = "Объявления",
            summary = "Удаление объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable int id) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        adsService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Эндпоинт для обновления информации об объявлении.
     *
     * @param id       Идентификатор объявления.
     * @param updateAd Объект, содержащий обновленные данные об объявлении.
     * @return Ответ, содержащий обновленное объявление.
     */
    @Operation(
            tags = "Объявления",
            summary = "Обновление информации об объявлении"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable int id, @RequestBody CreateOrUpdateAd updateAd) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        Ad ad = adsService.updateAd(id, updateAd);
        return ResponseEntity.ok(ad);
    }

    /**
     * Эндпоинт для получения объявлений авторизованного пользователя.
     *
     * @return Ответ, содержащий объявления пользователя.
     */
    @Operation(
            tags = "Объявления",
            summary = "Получение объявлений авторизованного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<Ads> getMyAds() {
        log.info("Использован метод {}", MethodLog.getMethodName());
        Ads ads = adsService.getAdsByUser();
        return ResponseEntity.ok(ads);
    }

    /**
     * Эндпоинт для обновления изображения объявления.
     *
     * @param id    Идентификатор объявления.
     * @param image Новое изображение объявления.
     * @return Ответ с кодом состояния 200, если обновление выполнено успешно.
     */
    @Operation(
            tags = "Объявления",
            summary = "Обновление картинки объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<Void> updateAdImage(@PathVariable int id, @RequestPart("image") MultipartFile image) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        adsService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }
}
