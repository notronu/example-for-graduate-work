package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

/**
 * Контроллер для управления изображениями.
 *
 * <p>Класс предоставляет API для получения аватара по указанному пути.</p>
 *
 * @CrossOrigin Разрешает запросы с указанного источника (http://localhost:3000).
 * @RestController Определяет данный класс как контроллер, который обрабатывает HTTP-запросы и возвращает данные в формате JSON/XML.
 * @RequestMapping Устанавливает базовый URL для запросов к данному контроллеру (C:/avatar).
 * @RequiredArgsConstructor Автоматически генерирует конструктор для final полей.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/C:/avatar")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Получение аватара по указанному пути.
     *
     * <p>Метод выполняет запрос для получения аватара из файловой системы по заданному пути.</p>
     *
     * @param path Путь к файлу аватара, который нужно получить.
     * @return {@link ResponseEntity} с байтовым массивом изображения, если операция успешна,
     * либо с ошибкой, если возникли проблемы при чтении файла.
     *
     * @Operation(summary = "Получение аватара по пути", description = "Возвращает аватар по указанному пути.")
     * Описание метода в контексте OpenAPI спецификации.
     *
     * @ApiResponses(value = {
     *   @ApiResponse(responseCode = "200", description = "Аватар успешно получен"),
     *   @ApiResponse(responseCode = "400", description = "Ошибка при получении аватара")
     * }) Описание возможных ответов API: успешное выполнение и ошибка.
     */
    @Operation(summary = "Получение аватара по пути",
            description = "Возвращает аватар по указанному пути.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аватар успешно получен"),
            @ApiResponse(responseCode = "400", description = "Ошибка при получении аватара")
    })
    @GetMapping(value = "/{path}")
    public ResponseEntity<byte[]> getUrl(@PathVariable String path) {
        try {
            byte[] byteFromFile = imageService.getByteFromFile(path);
            return ResponseEntity.ok(byteFromFile);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
