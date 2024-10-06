package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для управления сущностями пользователя {@link UserEntity}.
 * <p>
 * Этот интерфейс расширяет {@link JpaRepository}, предоставляя стандартные методы
 * для работы с базой данных, такие как сохранение, обновление, удаление и поиск по ID.
 * Также включает методы для поиска пользователей по имени пользователя и email.
 * </p>
 *
 * @author [Ваше имя]
 * @see JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Находит сущность пользователя по имени пользователя.
     * <p>
     * Этот метод выполняет поиск пользователя в базе данных по полю "username".
     * Если пользователя с таким именем нет, метод вернет {@code null}.
     * </p>
     *
     * @param username имя пользователя, по которому осуществляется поиск
     * @return объект {@link UserEntity}, соответствующий переданному имени пользователя, или {@code null}, если пользователь не найден
     */
    UserEntity findByUsername(String username);

    /**
     * Находит сущность пользователя по адресу электронной почты.
     * <p>
     * Этот метод возвращает {@link Optional}, содержащий пользователя, если он найден,
     * или пустой {@link Optional}, если пользователь с таким email не существует в базе данных.
     * </p>
     *
     * @param email электронная почта пользователя, по которой осуществляется поиск
     * @return {@link Optional} с найденным объектом {@link UserEntity} или пустой {@link Optional}, если пользователь не найден
     */
    Optional<UserEntity> findByEmail(String email);
}
