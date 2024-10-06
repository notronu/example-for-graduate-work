package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/C:/avatar")
@RequiredArgsConstructor
public class ImageController {
    //@Value("${path.to.avatars.folder}")

    private final ImageService imageService;

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
