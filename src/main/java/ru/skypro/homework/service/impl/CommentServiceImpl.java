package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utils.MapperUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления комментариями.
 * Предоставляет методы для получения, создания, обновления и удаления комментариев.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final UserRepository userRepository;

    /**
     * Получает список комментариев, связанных с определённым объявлением.
     *
     * @param adId идентификатор объявления.
     * @return список объектов {@link Comment}, связанных с объявлением.
     */
    @Override
    public List<Comment> getCommentsByAdId(int adId) {
        List<CommentEntity> commentEntities = commentRepository.findByAdId(adId);
        return commentEntities.stream().map(MapperUtils::toCommentDto).collect(Collectors.toList());
    }

    /**
     * Создаёт новый комментарий к объявлению.
     *
     * @param adId идентификатор объявления.
     * @param createOrUpdateComment объект {@link CreateOrUpdateComment}, содержащий данные нового комментария.
     * @return объект {@link Comment}, представляющий созданный комментарий.
     * @throws IllegalArgumentException если объявление с указанным идентификатором не найдено.
     */
    @Override
    public Comment createComment(int adId, CreateOrUpdateComment createOrUpdateComment) {
        CommentEntity commentEntity = MapperUtils.toCommentEntity(createOrUpdateComment);
        commentEntity.setAd(adsRepository.findById(adId)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено")));
        commentEntity.setCreatedAt(LocalDateTime.now());
        commentRepository.save(commentEntity);
        return MapperUtils.toCommentDto(commentEntity);
    }

    /**
     * Удаляет комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария.
     */
    @Override
    public void deleteComment(int commentId) {
        commentRepository.deleteById(commentId);
    }

    /**
     * Обновляет существующий комментарий.
     *
     * @param adId идентификатор объявления, к которому относится комментарий.
     * @param commentId идентификатор комментария, который требуется обновить.
     * @param createOrUpdateComment объект {@link CreateOrUpdateComment}, содержащий обновлённые данные комментария.
     * @return обновлённый объект {@link Comment}, или {@code null}, если комментарий не найден.
     */
    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment createOrUpdateComment) {
        Optional<CommentEntity> commentEntityOptional = commentRepository.findById(commentId);
        if (commentEntityOptional.isPresent()) {
            CommentEntity commentEntity = commentEntityOptional.get();
            commentEntity.setText(createOrUpdateComment.getText());
            commentRepository.save(commentEntity);
            return MapperUtils.toCommentDto(commentEntity);
        }
        return null;
    }

    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment createOrUpdateComment, String username) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));

        // Поиск пользователя
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        if (!commentEntity.getAuthor().getId().equals(userEntity.getId())) {
            throw new AccessDeniedException("Вы не являетесь автором этого комментария");
        }

        commentEntity.setText(createOrUpdateComment.getText());
        commentRepository.save(commentEntity);
        return MapperUtils.toCommentDto(commentEntity);
    }
}
