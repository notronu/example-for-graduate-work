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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.utils.MethodLog;

/**
 * Контроллер для обработки операций авторизации и регистрации пользователей.
 * Предоставляет эндпоинты для входа в систему и регистрации нового пользователя.
 */
@Slf4j
@RestController
@Tag(name = "Авторизация")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    /**
     * Эндпоинт для авторизации пользователя.
     * Принимает данные для входа и проверяет их корректность.
     *
     * @param login Объект, содержащий имя пользователя и пароль.
     * @return Ответ с кодом 200 в случае успешной авторизации,
     *         или с кодом 401, если авторизация не удалась.
     */
    @Operation(
            tags = "Авторизация",
            summary = "Авторизация пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Login login) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        boolean isLoggedIn = authService.login(login.getUsername(), login.getPassword());
        if (isLoggedIn) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Эндпоинт для регистрации нового пользователя.
     * Принимает данные для регистрации и создает нового пользователя в системе.
     *
     * @param register Объект, содержащий данные для регистрации (имя пользователя, пароль, и т.д.).
     * @return Ответ с кодом 201 в случае успешной регистрации,
     *         или с кодом 400, если данные некорректны.
     */
    @Operation(
            tags = "Регистрация",
            summary = "Регистрация пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody Register register) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        boolean isRegistered = authService.register(register);
        if (isRegistered) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}