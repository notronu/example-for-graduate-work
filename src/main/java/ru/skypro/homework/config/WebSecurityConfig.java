package ru.skypro.homework.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.impl.DatabaseUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    private final DatabaseUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Отключение CSRF (если нужно использовать JWT, включите его позже)
                .authorizeHttpRequests(authorize -> authorize
                        .mvcMatchers(AUTH_WHITELIST).permitAll() // Разрешить доступ к публичным ресурсам
                        .mvcMatchers("/users/**").hasAnyRole("USER", "ADMIN") // Доступ к /users только авторизованным пользователям
                        .mvcMatchers("/ads/**").authenticated() // Доступ к объявлениям только авторизованным пользователям
                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
                )
                .httpBasic() // Используем базовую аутентификацию
                .and()
                .formLogin(); // Форма входа
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Использование шифрования паролей
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService; // Используем кастомную реализацию UserDetailsService для загрузки пользователей из БД
    }
}