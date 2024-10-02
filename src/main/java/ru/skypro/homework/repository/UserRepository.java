package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с сущностями пользователей {@link UserEntity}.
 * Предоставляет методы для выполнения операций с базой данных, таких как сохранение, обновление, удаление и поиск пользователей.
 * Наследует основные методы для работы с сущностями от {@link JpaRepository}.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Находит пользователя по его имени пользователя (логину).
     *
     * @param username имя пользователя, по которому выполняется поиск.
     * @return объект {@link UserEntity}, представляющий пользователя с указанным именем, или {@code null}, если пользователь не найден.
     */
    UserEntity findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);
}
