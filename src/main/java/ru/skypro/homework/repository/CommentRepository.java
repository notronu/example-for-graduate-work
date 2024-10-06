package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;

/**
 * Репозиторий для работы с сущностями комментариев {@link CommentEntity}.
 * Предоставляет методы для выполнения операций с базой данных, таких как сохранение, обновление, удаление и поиск комментариев.
 * Наследует основные методы для работы с сущностями от {@link JpaRepository}.
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Возвращает список комментариев, относящихся к объявлению с указанным идентификатором.
     *
     * @param adId идентификатор объявления, к которому относятся комментарии.
     * @return список комментариев, связанных с указанным объявлением.
     */
    List<CommentEntity> findByAdId(Integer adId);

    @Query("SELECT COUNT(c) > 0 FROM CommentEntity c WHERE c.author = :author AND c.ad = :ad AND c.text = :text")
    boolean existsByAuthorAndAdAndText(@Param("author") UserEntity author, @Param("ad") AdEntity ad, @Param("text") String text);
}
