package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Репозиторий для работы с сущностями объявлений {@link AdEntity}.
 * Предоставляет методы для выполнения операций с базой данных, таких как сохранение, обновление, удаление и поиск объявлений.
 * Наследует основные методы для работы с сущностями от {@link JpaRepository}.
 */
public interface AdsRepository extends JpaRepository<AdEntity, Integer> {

    /**
     * Возвращает список объявлений, созданных пользователем с указанным идентификатором автора.
     *
     * @param authorId идентификатор автора объявлений.
     * @return список объявлений, принадлежащих указанному автору.
     */
    List<AdEntity> findByAuthorId(Integer authorId);
}