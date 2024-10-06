package ru.skypro.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурационный класс для настройки безопасности веб-приложения.
 * Этот класс использует аннотации {@link Configuration} и {@link EnableWebSecurity}
 * для того, чтобы включить функциональность безопасности Spring Security и настроить фильтрацию запросов.
 * <p>
 * Основные возможности, которые предоставляются этим классом:
 * - Настройка разрешённых и защищённых URL-адресов.
 * - Отключение CSRF-защиты (не рекомендуется для production-среды, если не используются специальные меры защиты).
 * - Настройка CORS (доступ с других доменов).
 * - Настройка базовой HTTP-аутентификации.
 * - Определение паролей, которые будут шифроваться с помощью {@link BCryptPasswordEncoder}.
 * <p>
 * Аннотация {@link EnableWebSecurity} позволяет интегрировать Spring Security в приложение,
 * а аннотация {@link Configuration} делает этот класс конфигурационным, чтобы его бины могли
 * использоваться другими компонентами системы.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Массив строк, который содержит пути к ресурсам, доступным без аутентификации.
     * Эти URL-адреса относятся к Swagger-документации и страницам регистрации/входа.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api-docs/**",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Создаёт и настраивает цепочку фильтров безопасности для обработки HTTP-запросов.
     *
     * <p>Этот метод отключает защиту CSRF, разрешает определённые URL-адреса
     * (определённые в {@link #AUTH_WHITELIST}) для общего доступа без аутентификации,
     * и требует аутентификации для всех запросов на "/ads/**" и "/users/**".</p>
     *
     * @param http объект {@link HttpSecurity}, предоставляемый Spring Security для конфигурации безопасности.
     * @return настроенный объект {@link SecurityFilterChain} для фильтрации запросов.
     * @throws Exception если возникает ошибка при настройке безопасности.
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
     * Определяет и создаёт бин для кодирования паролей с использованием алгоритма BCrypt.
     *
     * <p>Этот метод возвращает экземпляр {@link BCryptPasswordEncoder},
     * который будет использоваться для шифрования паролей пользователей перед их хранением в базе данных.
     * BCrypt является безопасным методом шифрования, который адаптируется с ростом вычислительных мощностей.</p>
     *
     * @return объект {@link PasswordEncoder}, который будет использоваться для шифрования паролей.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}