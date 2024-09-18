package ru.skypro.homework.utils;

import ru.skypro.homework.dto.*;
import ru.skypro.homework.entity.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
        user.setRole(userEntity.getRole().name());
        user.setImage(userEntity.getImage());
        return user;
    }

    /**
     * Преобразует объект {@link AdEntity} в объект {@link Ad} (DTO).
     *
     * @param adEntity объект сущности объявления.
     * @return объект DTO объявления.
     */
    public static Ad toAdDto(AdEntity adEntity) {
        Ad ad = new Ad();
        ad.setPk(adEntity.getId());
        ad.setTitle(adEntity.getTitle());
        ad.setPrice(adEntity.getPrice());
        ad.setImage(adEntity.getImage());
        return ad;
    }

    /**
     * Преобразует объект {@link AdEntity} в объект {@link ExtendedAd} (расширенный DTO).
     *
     * @param adEntity объект сущности объявления.
     * @return расширенный объект DTO объявления.
     */
    public static ExtendedAd toExtendedAdDto(AdEntity adEntity) {
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(adEntity.getId());
        extendedAd.setTitle(adEntity.getTitle());
        extendedAd.setPrice(adEntity.getPrice());
        extendedAd.setDescription(adEntity.getDescription());
        extendedAd.setImage(adEntity.getImage());
        extendedAd.setAuthorFirstName(adEntity.getAuthor().getFirstName());
        extendedAd.setAuthorLastName(adEntity.getAuthor().getLastName());
        extendedAd.setEmail(adEntity.getAuthor().getUsername());
        extendedAd.setPhone(adEntity.getAuthor().getPhone());
        return extendedAd;
    }

    /**
     * Преобразует объект {@link CommentEntity} в объект {@link Comment} (DTO).
     *
     * @param commentEntity объект сущности комментария.
     * @return объект DTO комментария.
     */
    public static Comment toCommentDto(CommentEntity commentEntity) {
        Comment comment = new Comment();
        comment.setPk(commentEntity.getId());
        comment.setText(commentEntity.getText());
        comment.setAuthor(commentEntity.getAuthor().getId());
        comment.setAuthorFirstName(commentEntity.getAuthor().getFirstName());
        // Преобразование LocalDateTime в миллисекунды
        String formattedDateTime = commentEntity.getCreatedAt().format(DateTimeFormatter.ISO_INSTANT);
        comment.setCreatedAt(LocalDateTime.parse(formattedDateTime));
        return comment;
    }

    /**
     * Преобразует объект {@link CreateOrUpdateAd} (DTO) в объект {@link AdEntity}.
     *
     * @param createOrUpdateAd объект DTO для создания или обновления объявления.
     * @return объект сущности объявления.
     */
    public static AdEntity toAdEntity(CreateOrUpdateAd createOrUpdateAd) {
        AdEntity adEntity = new AdEntity();
        adEntity.setTitle(createOrUpdateAd.getTitle());
        adEntity.setPrice(createOrUpdateAd.getPrice());
        adEntity.setDescription(createOrUpdateAd.getDescription());
        return adEntity;
    }

    /**
     * Преобразует объект {@link CreateOrUpdateComment} (DTO) в объект {@link CommentEntity}.
     *
     * @param createOrUpdateComment объект DTO для создания или обновления комментария.
     * @return объект сущности комментария.
     */
    // Обратное преобразование из метки времени (long) в LocalDateTime
    public static CommentEntity toCommentEntity(CreateOrUpdateComment createOrUpdateComment) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(createOrUpdateComment.getText());

        // Пример: long timestamp = 1634598123000L; (в миллисекундах)
        long timestamp = System.currentTimeMillis(); // Замените на ваш timestamp
        LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);

        commentEntity.setCreatedAt(createdAt); // Установка времени создания комментария
        return commentEntity;
    }
}