package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.exception.NotFoundException;

import java.io.IOException;

/**
 * Контроллер для управления объявлениями.
 * Предоставляет операции для получения, создания, обновления и удаления объявлений, а также загрузки изображений.
 * Операции доступны как для аутентифицированных пользователей, так и для администраторов.
 *
 * @CrossOrigin(value = "http://localhost:3000") разрешает запросы с фронтенда, размещенного на localhost:3000.
 * @RestController обозначает, что класс является контроллером Spring и возвращает JSON в качестве ответа.
 * @RequiredArgsConstructor генерирует конструктор с обязательными полями, которыми являются final-поля.
 * @RequestMapping("/ads") задает базовый URL для всех эндпоинтов контроллера.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;

    /**
     * Возвращает список всех объявлений.
     *
     * @return ResponseEntity с JSON-списком всех объявлений и статусом 200.
     */
    @Operation(summary = "Получить все объявления", description = "Возвращает список всех объявлений.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений получен успешно",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))})
    })
    @GetMapping
    public ResponseEntity<Ads> getAllAds() {
        return ResponseEntity.ok(adsService.getAll());
    }

    /**
     * Возвращает объявления текущего аутентифицированного пользователя.
     *
     * @param authentication объект, содержащий данные об аутентификации пользователя.
     * @return ResponseEntity с JSON-списком объявлений пользователя и статусом 200.
     */
    @Operation(summary = "Получить объявления пользователя", description = "Возвращает объявления текущего аутентифицированного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявления пользователя получены успешно",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ads.class))})
    })
    @GetMapping("/me")
    public ResponseEntity<Ads> getAdsMe(Authentication authentication) {
        Ads ads = adsService.getMyAds(authentication.getName());
        return ResponseEntity.ok(ads);
    }

    /**
     * Возвращает детализированную информацию об объявлении по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return ResponseEntity с JSON-данными объявления и статусом 200 или 404, если объявление не найдено.
     */
    @Operation(summary = "Получить объявление по ID", description = "Возвращает детализированную информацию об объявлении по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление получено успешно",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExtendedAd.class))}),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAd(@PathVariable Integer id) {
        return ResponseEntity.ok(adsService.getExtendedAd(id));
    }

    /**
     * Возвращает изображение объявления по его идентификатору.
     *
     * @param id идентификатор объявления.
     * @return ResponseEntity с изображением в формате JPEG или PNG и статусом 200 или 404, если изображение не найдено.
     * @throws IOException если произошла ошибка при загрузке изображения.
     */
    @Operation(summary = "Загрузить изображение", description = "Возвращает изображение объявления по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображение получено успешно",
                    content = {@Content(mediaType = "image/jpeg")}),
            @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    @GetMapping(value = "{id}/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) throws IOException {
        return ResponseEntity.ok(adsService.getImage(id));
    }

    /**
     * Создает новое объявление с изображением.
     *
     * @param createOrUpdateAd объект с данными объявления.
     * @param image            изображение для объявления.
     * @param authentication   объект аутентификации текущего пользователя.
     * @return ResponseEntity с JSON-данными созданного объявления и статусом 201 или 400 в случае ошибки.
     * @throws IOException если произошла ошибка при загрузке изображения.
     */
    @Operation(summary = "Создать новое объявление", description = "Создает новое объявление с изображением.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Объявление создано успешно",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка создания объявления")
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Ad> addAd(@RequestPart(value = "properties") CreateOrUpdateAd createOrUpdateAd,
                                    @RequestPart(value = "image") MultipartFile image,
                                    Authentication authentication) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsService.addAd(createOrUpdateAd, image, authentication));
    }

    /**
     * Обновляет объявление по его идентификатору.
     *
     * @param id               идентификатор объявления.
     * @param createOrUpdateAd объект с новыми данными объявления.
     * @param authentication   объект аутентификации текущего пользователя.
     * @return ResponseEntity с обновленными данными объявления и статусом 200, либо 403 или 404 в случае ошибки.
     */
    @Operation(summary = "Обновить объявление", description = "Обновляет объявление по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объявление обновлено успешно",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Ad.class))}),
            @ApiResponse(responseCode = "403", description = "Нет прав на изменение объявления"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.getAd(#id).user.email == authentication.principal.username")
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Integer id,
                                       @RequestBody CreateOrUpdateAd createOrUpdateAd,
                                       Authentication authentication) {
        return ResponseEntity.ok(adsService.updateAd(id, createOrUpdateAd, authentication));
    }

    /**
     * Обновляет изображение объявления.
     * Доступно для администратора или владельца объявления.
     *
     * @param id             идентификатор объявления.
     * @param image          новое изображение для объявления.
     * @param authentication объект аутентификации текущего пользователя.
     * @return ResponseEntity с обновленным изображением и статусом 200, либо 400, 401, 403 или 404 в случае ошибки.
     * @throws IOException если произошла ошибка при загрузке изображения.
     */
    @Operation(summary = "Обновить изображение объявления", description = "Доступно только для администратора или владельца объявления. Метод позволяет обновить изображение объявления.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено",
                    content = {@Content(mediaType = "image/jpeg"), @Content(mediaType = "image/png")}),
            @ApiResponse(responseCode = "400", description = "Некорректный запрос"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.getAd(#id).user.email == authentication.principal.username")
    @PatchMapping(value = "{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> updateAdImage(@PathVariable Integer id,
                                                @RequestParam MultipartFile image,
                                                Authentication authentication) throws IOException {
        return ResponseEntity.ok(adsService.updateAdImage(id, image, authentication));
    }

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id             идентификатор объявления.
     * @param authentication объект аутентификации текущего пользователя.
     * @return ResponseEntity с статусом 204, если объявление удалено, либо 403 или 404 в случае ошибки.
     */
    @Operation(summary = "Удалить объявление", description = "Удаляет объявление по его идентификатору.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Объявление удалено успешно"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление объявления"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено")
    })
    @PreAuthorize("hasAuthority('ADMIN') or @adServiceImpl.getAd(#id).user.email == authentication.principal.username")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Integer id, Authentication authentication) {
        try {
            adsService.deleteAd(id, authentication);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}