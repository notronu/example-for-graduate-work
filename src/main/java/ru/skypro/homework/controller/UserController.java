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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;

import org.springframework.security.core.Authentication;

import java.io.IOException;

import ru.skypro.homework.exception.PasswordIsNotCorrectException;
import ru.skypro.homework.service.impl.UserServiceImpl;

import javax.validation.Valid;

/**
 * Контроллер для управления пользователями.
 * Предоставляет операции для получения и обновления информации о пользователях, а также смены пароля.
 *
 * Этот контроллер поддерживает кросс-оригинальные запросы с домена http://localhost:3000.
 * Аннотация @CrossOrigin позволяет обрабатывать запросы с фронтенда, расположенного на этом домене.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    /**
     * Сервис для управления операциями, связанными с пользователями.
     * Используется для выполнения операций по обновлению пароля, получения информации и т.д.
     */
    private final UserServiceImpl userService;

    /**
     * Устанавливает новый пароль для пользователя.
     *
     * @param dto объект типа {@link NewPassword}, содержащий новый пароль
     * @param authentication объект {@link Authentication}, представляющий текущего аутентифицированного пользователя
     * @return объект {@link ResponseEntity}, содержащий статус операции и объект {@link NewPassword}
     *
     * @throws PasswordIsNotCorrectException если текущий пароль, предоставленный пользователем, неверен
     *
     * @apiNote Эта операция позволяет пользователю сменить свой пароль. Если текущий пароль неверен, возвращается ошибка с кодом 403.
     *
     * @see NewPassword
     * @see PasswordIsNotCorrectException
     */
    @Operation(summary = "Установить новый пароль",
            description = "Эта операция позволяет пользователю сменить свой пароль.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пароль успешно обновлен",
                    content = @Content(schema = @Schema(implementation = NewPassword.class))),
            @ApiResponse(responseCode = "403", description = "Неверный текущий пароль")
    })
    @PostMapping("/set_password")
    public ResponseEntity<NewPassword> setPassword(@Valid @RequestBody NewPassword dto, Authentication authentication) {
        try {
            userService.updatePassword(dto, authentication.getName());
        } catch (PasswordIsNotCorrectException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
        }

        return ResponseEntity.ok(dto);
    }

    /**
     * Возвращает информацию о текущем пользователе.
     *
     * @param authentication объект {@link Authentication}, представляющий текущего аутентифицированного пользователя
     * @return объект {@link ResponseEntity}, содержащий объект {@link User} с информацией о пользователе
     *
     * @apiNote Операция возвращает основную информацию о текущем пользователе на основе его аутентификации.
     *
     * @see User
     */
    @Operation(summary = "Получить информацию о пользователе",
            description = "Возвращает информацию о текущем пользователе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена",
                    content = @Content(schema = @Schema(implementation = User.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getInfoAboutMe(authentication.getName()));
    }

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * @param dto объект типа {@link UpdateUser}, содержащий новую информацию о пользователе
     * @param authentication объект {@link Authentication}, представляющий текущего аутентифицированного пользователя
     * @return объект {@link ResponseEntity}, содержащий обновленную информацию о пользователе
     *
     * @apiNote Операция обновляет информацию о пользователе, такую как имя, адрес электронной почты и другие данные.
     *
     * @see UpdateUser
     */
    @Operation(summary = "Обновить информацию о пользователе",
            description = "Эта операция позволяет обновить информацию о текущем пользователе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация успешно обновлена",
                    content = @Content(schema = @Schema(implementation = UpdateUser.class)))
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@Valid @RequestBody UpdateUser dto, Authentication authentication) {
        return ResponseEntity.ok(userService.updateInfoAboutMe(authentication.getName(), dto));
    }

    /**
     * Обновляет изображение профиля пользователя.
     *
     * @param image объект типа {@link MultipartFile}, представляющий новое изображение
     * @param authentication объект {@link Authentication}, представляющий текущего аутентифицированного пользователя
     * @return объект {@link ResponseEntity} со статусом операции
     *
     * @throws IOException если произошла ошибка при сохранении изображения
     *
     * @apiNote Эта операция загружает новое изображение профиля пользователя.
     */
    @Operation(summary = "Обновить изображение пользователя",
            description = "Эта операция позволяет загрузить новое изображение профиля пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображение успешно обновлено")
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image, Authentication authentication) throws IOException {
        userService.updateMyImage(authentication.getName(), image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Возвращает изображение профиля указанного пользователя.
     *
     * @param username имя пользователя, чье изображение запрашивается
     * @return объект {@link ResponseEntity}, содержащий изображение в формате байтового массива
     *
     * @apiNote Эта операция возвращает изображение профиля по имени пользователя. Изображение может быть в формате JPEG или PNG.
     */
    @Operation(summary = "Получить изображение пользователя",
            description = "Эта операция возвращает изображение профиля указанного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Изображение успешно получено",
                    content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE))
    })
    @GetMapping(value = "{username}/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getUserImage(@PathVariable String username) {
        return ResponseEntity.ok(userService.getImage(username));
    }
}
