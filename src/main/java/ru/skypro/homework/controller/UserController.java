package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.MethodLog;
import org.springframework.security.core.Authentication;

import java.io.IOException;

/**
 * Контроллер для обработки операций, связанных с пользователями.
 * Предоставляет эндпоинты для управления паролем, получения и обновления информации о текущем пользователе,
 * а также обновления аватара пользователя .
 */
@Slf4j
@RestController
@Tag(name = "Пользователи")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Эндпоинт для обновления пароля авторизованного пользователя.
     *
     * @param newPassword Объект, содержащий текущий и новый пароль пользователя.
     * @return Ответ с кодом 200, если пароль был успешно обновлен, или с кодом 403, если обновление не удалось.
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление пароля"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody NewPassword newPassword) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        boolean isPasswordChanged = userService.setNewPassword(newPassword);
        if (isPasswordChanged) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * Эндпоинт для получения информации о текущем авторизованном пользователе.
     *
     * @return Ответ с кодом 200, содержащий информацию о пользователе.
     */
    @Operation(
            tags = "Пользователи",
            summary = "Получение информации об авторизованном пользователе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    public ResponseEntity<User> getUser() {
        log.info("Использован метод {}", MethodLog.getMethodName());
        User user = userService.getUser();
        return ResponseEntity.ok(user);
    }

    /**
     * Эндпоинт для обновления информации о текущем авторизованном пользователе.
     *
     * @param updateUser Объект, содержащий обновленные данные пользователя.
     * @return Ответ с кодом 200, содержащий обновленную информацию о пользователе.
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление информации об авторизованном пользователе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UpdateUser.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@RequestBody UpdateUser updateUser) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        UpdateUser updatedUser = userService.updateUser(updateUser);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Эндпоинт для обновления аватара текущего авторизованного пользователя.
     *
     * @param image Новое изображение профиля (аватар).
     * @return Ответ с кодом 200 в случае успешного обновления аватара.
     */
    @Operation(
            tags = "Пользователи",
            summary = "Обновление аватара авторизованного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image, Authentication authentication) throws IOException {
        userService.updateMyImage(authentication.getName(), image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "{username}/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getUserImage(@PathVariable String username) {
        return ResponseEntity.ok(userService.getImage(username));
    }

}
