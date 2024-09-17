package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.dto.Role;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Класс конфигурации безопасности веб-приложения.
 * Определяет правила аутентификации и авторизации пользователей,
 * а также предоставляет компоненты для кодирования паролей.
 */
@Configuration
public class WebSecurityConfig {

    /**
     * Список URL-адресов, которые не требуют аутентификации.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Создает и настраивает InMemoryUserDetailsManager с одним пользователем.
     *
     * @param passwordEncoder Кодировщик паролей для шифрования пароля пользователя.
     * @return InMemoryUserDetailsManager, содержащий информацию о пользователе.
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user =
                User.builder()
                        .username("user@gmail.com")
                        .password("password")
                        .passwordEncoder(passwordEncoder::encode)
                        .roles(Role.USER.name())
                        .build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * Конфигурирует цепочку фильтров безопасности для обработки HTTP-запросов.
     * Отключает CSRF-защиту, настраивает URL-адреса, которые требуют аутентификации, и разрешает
     * доступ к указанным ресурсам без аутентификации.
     *
     * @param http Объект HttpSecurity для настройки политики безопасности.
     * @return Настроенная цепочка фильтров безопасности.
     * @throws Exception если возникает ошибка конфигурации.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Создает и возвращает кодировщик паролей на основе алгоритма BCrypt.
     *
     * @return Объект PasswordEncoder для кодирования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}