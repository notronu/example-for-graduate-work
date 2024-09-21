package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * Сервис для обработки логики авторизации и регистрации пользователей.
 * Реализует методы для входа в систему и регистрации новых пользователей.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Выполняет проверку имени пользователя и пароля при авторизации.
     *
     * @param username имя пользователя для входа в систему.
     * @param password пароль для входа.
     * @return {@code true}, если авторизация успешна, иначе {@code false}.
     */
    @Override
    public boolean login(String username, String password) {
        if (!userDetailsManager.userExists(username)) {
            return false;
        }
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        return passwordEncoder.matches(password, userDetails.getPassword());
    }

    /**
     * Регистрирует нового пользователя, если имя пользователя еще не используется.
     *
     * @param register объект {@link Register}, содержащий данные для регистрации (имя пользователя, пароль и роль).
     * @return {@code true}, если регистрация успешна, иначе {@code false}, если имя пользователя уже существует.
     */
    @Override
    public boolean register(Register register) {
        if (userDetailsManager.userExists(register.getUsername())) {
            return false;
        }
        userDetailsManager.createUser(
                User.builder()
                        .passwordEncoder(passwordEncoder::encode)
                        .username(register.getUsername())
                        .password(register.getPassword())
                        .roles(register.getRole().name())
                        .build());
        return true;
    }

}


