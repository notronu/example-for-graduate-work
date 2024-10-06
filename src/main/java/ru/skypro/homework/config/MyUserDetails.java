package ru.skypro.homework.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.entity.UserEntity;

import java.util.Collection;
import java.util.List;

/**
 * Класс, реализующий интерфейс {@link UserDetails} для обеспечения
 * интеграции пользовательских данных с системой безопасности Spring Security.
 * Используется для работы с объектом {@link UserEntity}, содержащим информацию о пользователе.
 */
public class MyUserDetails implements UserDetails {
    private UserEntity userEntity;

    /**
     * Конструктор для создания объекта {@code MyUserDetails}.
     *
     * @param userEntity объект {@link UserEntity}, представляющий пользователя.
     */
    public MyUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    /**
     * Возвращает список прав пользователя.
     *
     * @return коллекция объектов {@link GrantedAuthority}, содержащая права пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userEntity.getRole().name()));
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return строка с паролем пользователя.
     */
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    /**
     * Возвращает имя пользователя, которое в данном случае представлено его email.
     *
     * @return строка с email пользователя.
     */
    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    /**
     * Проверяет, истек ли срок действия учетной записи.
     * В данном случае всегда возвращает {@code true}, так как аккаунты не истекают.
     *
     * @return {@code true}, если аккаунт действителен.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, заблокирован ли аккаунт.
     * В данном случае всегда возвращает {@code true}, так как аккаунты не блокируются.
     *
     * @return {@code true}, если аккаунт не заблокирован.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, истек ли срок действия учетных данных.
     * В данном случае всегда возвращает {@code true}, так как учетные данные не истекают.
     *
     * @return {@code true}, если учетные данные действительны.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активирован ли пользователь.
     * В данном случае всегда возвращает {@code true}, так как пользователи всегда активны.
     *
     * @return {@code true}, если пользователь активен.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}