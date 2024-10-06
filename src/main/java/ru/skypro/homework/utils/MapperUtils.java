package ru.skypro.homework.utils;

import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

/**
 * Утилитарный класс для преобразования сущностей (Entity) в объекты передачи данных (DTO) и обратно.
 * Содержит статические методы для конвертации между различными сущностями и DTO.
 */
public class MapperUtils {

    /**
     * Преобразует объект {@link UserEntity} в объект {@link User} (DTO).
     *
     * @param userEntity объект сущности пользователя.
     * @return объект DTO пользователя.
     */
    public static User toUserDto(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setEmail(userEntity.getUsername());
        user.setPhone(userEntity.getPhone());
        //   user.setRole(userEntity.getRole().name());
        user.setImage(userEntity.getImage());
        return user;
    }

    /**
     * Преобразует объект {@link CommentEntity} в объект {@link Comment} (DTO).
     *
     * @param commentEntity объект сущности комментария.
     * @return объект DTO комментария.
     */
    //   public static Comment toCommentDto(CommentEntity commentEntity) {
    //       Comment comment = new Comment();
    //      comment.setPk(commentEntity.getId());
    //       comment.setText(commentEntity.getText());
    //      comment.setAuthor(commentEntity.getAuthor().getId());
    //       comment.setAuthorFirstName(commentEntity.getAuthor().getFirstName());

    //       String formattedDateTime = commentEntity.getCreatedAt().format(DateTimeFormatter.ISO_INSTANT);
    //      comment.setCreatedAt(LocalDateTime.parse(formattedDateTime));
    //      return comment;
    //   }

    // Обратное преобразование из метки времени (long) в LocalDateTime
//    public static CommentEntity toCommentEntity(CreateOrUpdateComment createOrUpdateComment) {
    //       CommentEntity commentEntity = new CommentEntity();
    //       commentEntity.setText(createOrUpdateComment.getText());

    //       // Пример: long timestamp = 1634598123000L; (в миллисекундах)
    //     long timestamp = System.currentTimeMillis(); // Замените на ваш timestamp
    ////       LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);

    //      commentEntity.setCreatedAt(createdAt); // Установка времени создания комментария
    //      return commentEntity;
    //   }
}
