package ru.skypro.homework.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

/**
 * Сервис для предоставления деталей пользователя по его имени пользователя (email).
 * Реализует интерфейс {@link UserDetailsService}, который используется Spring Security
 * для получения информации о пользователе для аутентификации.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Конструктор для внедрения зависимости {@link UserRepository}.
     *
     * @param userRepository репозиторий пользователей, используемый для поиска пользователей по email.
     */
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Загружает данные о пользователе по его имени (email).
     * Используется для аутентификации пользователя в системе.
     *
     * @param username имя пользователя (email), для которого нужно загрузить данные.
     * @return объект {@link MyUserDetails}, содержащий детали пользователя.
     * @throws UsernameNotFoundException если пользователь с указанным email не найден.
     */
    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        return new MyUserDetails(userEntity);
    }
}
