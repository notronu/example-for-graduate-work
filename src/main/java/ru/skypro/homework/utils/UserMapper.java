package ru.skypro.homework.utils;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.entity.UserEntity;

/**
 * Сервис для преобразования между объектами сущности пользователя {@link UserEntity}
 * и объектами DTO пользователя {@link User}.
 * <p>
 * Класс предоставляет статические методы для преобразования сущностей и DTO
 * для использования в слоях приложения, таких как сервисы и контроллеры.
 * </p>
 */
@Service
public class UserMapper {

    /**
     * Преобразует объект сущности пользователя {@link UserEntity} в DTO {@link User}.
     * <p>
     * Метод создает новый объект {@link User} и заполняет его данными из {@link UserEntity}.
     * Поле {@code image} формируется на основе адреса изображения пользователя.
     * </p>
     *
     * @param userEntity объект сущности пользователя, который необходимо преобразовать
     * @return объект DTO пользователя
     */
    public static User toDto(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setEmail(userEntity.getEmail());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setPhone(userEntity.getPhone());
        user.setRole(userEntity.getRole());
        user.setImage("/users/" + userEntity.getEmail() + "/image");
        return user;
    }

    /**
     * Преобразует объект DTO {@link Register}, полученный при регистрации пользователя,
     * в сущность пользователя {@link UserEntity}.
     * <p>
     * Метод создает новый объект {@link UserEntity} и заполняет его данными из {@link Register}.
     * Поля, такие как email и username, преобразуются в нижний регистр.
     * </p>
     *
     * @param dto объект DTO регистрации, содержащий информацию о пользователе
     * @return объект сущности пользователя
     */
    public static UserEntity toEntity(Register dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(dto.getUsername().toLowerCase());
        userEntity.setPhone(dto.getPhone());
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setRole(dto.getRole());
        userEntity.setUsername(dto.getUsername().toLowerCase());
        return userEntity;
    }
}
