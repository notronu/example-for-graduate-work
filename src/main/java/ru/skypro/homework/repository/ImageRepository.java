package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.ImageEntity;

/**
 * Репозиторий для работы с сущностями {@link ImageEntity}.
 *
 * <p>Предоставляет основные методы для взаимодействия с базой данных,
 * такие как сохранение, удаление, поиск по идентификатору и др.,
 * за счет расширения {@link JpaRepository}. Дополнительно,
 * включает кастомный метод для поиска сущности {@link ImageEntity}
 * по пути изображения.
 *
 * <p>Используется для доступа к данным о изображениях.
 *
 * <p>Аннотация {@link Repository} указывает, что данный интерфейс является
 * репозиторием, который обрабатывается Spring Data JPA для автоматической
 * генерации реализации в рантайме.
 */
@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer> {

    /**
     * Ищет сущность {@link ImageEntity} по указанному пути.
     *
     * <p>Данный метод позволяет находить изображение в базе данных
     * по полю {@code path}.
     *
     * @param path путь к изображению, для которого нужно найти сущность
     * @return объект {@link ImageEntity}, соответствующий указанному пути,
     * или {@code null}, если изображение не найдено
     */
    ImageEntity findByPath(String path);
}
