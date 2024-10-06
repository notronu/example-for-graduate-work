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

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServiceImpl userService;

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

    @Operation(summary = "Получить информацию о пользователе",
            description = "Возвращает информацию о текущем пользователе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Информация о пользователе успешно получена",
                    content = @Content(schema = @Schema(implementation = User.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(Authentication authentication) {
        return ResponseEntity.ok(userService.getInfoAboutMe(authentication.getName()));
    }

    @Operation(summary = "Обновить информацию о пользователе",
            description = "Эта операция позволяет обновить информацию о текущем пользователе.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Информация успешно обновлена",
                    content = @Content(schema = @Schema(implementation = UpdateUser.class)))
    })
    @PatchMapping("/me")
    public ResponseEntity<UpdateUser> updateUser(@Valid @RequestBody UpdateUser dto, Authentication authentication) {
        return ResponseEntity.ok(userService.updateInfoAboutMe(authentication.getName(), dto));
    }

    @Operation(summary = "Обновить изображение пользователя",
            description = "Эта операция позволяет загрузить новое изображение профиля пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Изображение успешно обновлено")
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image, Authentication authentication) throws IOException {
        userService.updateMyImage(authentication.getName(), image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Получить изображение пользователя",
            description = "Эта операция возвращает изображение профиля указанного пользователя.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Изображение успешно получено",
                    content = @Content(mediaType = MediaType.IMAGE_JPEG_VALUE))
    })
    @GetMapping(value = "{username}/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getUserImage(@PathVariable String username) {
        return ResponseEntity.ok(userService.getImage(username));
    }
}
