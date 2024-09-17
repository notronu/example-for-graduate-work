package ru.skypro.homework.service;

import ru.skypro.homework.dto.*;

import java.util.List;

/**
 * Сервис для работы с комментариями к объявлениям.
 * Предоставляет методы для получения, создания, обновления и удаления комментариев.
 */
public interface CommentService {

    /**
     * Получает список комментариев для конкретного объявления.
     *
     * @param adId идентификатор объявления.
     * @return список объектов {@link Comment}, связанных с объявлением.
     */
    List<Comment> getCommentsByAdId(int adId);

    /**
     * Создает новый комментарий для объявления.
     *
     * @param adId идентификатор объявления, к которому будет добавлен комментарий.
     * @param createOrUpdateComment объект {@link CreateOrUpdateComment}, содержащий текст комментария.
     * @return объект {@link Comment}, представляющий созданный комментарий.
     */
    Comment createComment(int adId, CreateOrUpdateComment createOrUpdateComment);

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария, который нужно удалить.
     */
    void deleteComment(int commentId);

    /**
     * Обновляет существующий комментарий.
     *
     * @param adId идентификатор объявления, к которому относится комментарий.
     * @param commentId идентификатор комментария, который нужно обновить.
     * @param createOrUpdateComment объект {@link CreateOrUpdateComment}, содержащий обновленный текст комментария.
     * @return обновленный объект {@link Comment}.
     */
    Comment updateComment(int adId, int commentId, CreateOrUpdateComment createOrUpdateComment);
}