package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * Репозиторий для работы с сущностями комментариев {@link CommentEntity}.
 * Предоставляет методы для выполнения операций с базой данных, таких как сохранение, обновление, удаление и поиск комментариев.
 * Наследует основные методы для работы с сущностями от {@link JpaRepository}.
 */
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Возвращает список комментариев, относящихся к объявлению с указанным идентификатором.
     *
     * @param adId идентификатор объявления, к которому относятся комментарии.
     * @return список комментариев, связанных с указанным объявлением.
     */
    List<CommentEntity> findByAdId(Integer adId);
}
