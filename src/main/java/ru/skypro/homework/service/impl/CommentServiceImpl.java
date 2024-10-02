package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utils.MapperUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Comment> getCommentsByAdId(int adId) {
        List<CommentEntity> commentEntities = commentRepository.findByAdId(adId);
        return commentEntities.stream()
                .map(MapperUtils::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment createComment(int adId, CreateOrUpdateComment createOrUpdateComment) {
        return null;
    }

    @Override
    public void deleteComment(int commentId) {

    }

    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment createOrUpdateComment) {
        return null;
    }

    @Override
    public Comment createComment(int adId, CreateOrUpdateComment createOrUpdateComment, String username) {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        CommentEntity commentEntity = MapperUtils.toCommentEntity(createOrUpdateComment);
        // Убедитесь, что у CommentEntity есть связь с AdEntity (это зависит от вашей схемы БД)
        commentEntity.setAd(new AdEntity());
        commentEntity.setAuthor(userEntity);

        commentRepository.save(commentEntity);
        return MapperUtils.toCommentDto(commentEntity);
    }

    @Override
    public Comment updateComment(int adId, int commentId, CreateOrUpdateComment updateComment, String username) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));

        UserEntity currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        // Проверка прав пользователя: либо автор комментария, либо администратор
        if (!commentEntity.getAuthor().getUsername().equals(currentUser.getUsername()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Вы не можете редактировать этот комментарий");
        }

        commentEntity.setText(updateComment.getText());
        commentRepository.save(commentEntity);

        return MapperUtils.toCommentDto(commentEntity);
    }

    @Override
    public void deleteComment(int commentId, String username) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Комментарий не найден"));

        UserEntity currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }

        // Проверка прав пользователя: либо автор комментария, либо администратор
        if (!commentEntity.getAuthor().getUsername().equals(currentUser.getUsername()) && !currentUser.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException("Вы не можете удалить этот комментарий");
        }

        commentRepository.delete(commentEntity);
    }
}
