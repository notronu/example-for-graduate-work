package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdsRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utils.CommentMapper;

import org.springframework.security.core.Authentication;

import ru.skypro.homework.exception.NotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdsRepository adsRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;

    @Override
    public Comments getComments(Integer adId, Authentication authentication) {
        if (adsRepository.existsById(adId)) {
            return commentMapper.toCommentsDto(commentRepository.findByAdId(adId));
        }
        else {
            throw new NotFoundException("Ad is not found");
        }
    }

    public Comment addComment(Integer pk, CreateOrUpdateComment dto, Authentication authentication) {
        if (adsRepository.existsById(pk)){
            UserEntity userEntity = userRepository.findByEmail(authentication.getName()).orElseThrow(RuntimeException::new);
            AdEntity adEntity = adsRepository.findById(pk).orElse(null);
            CommentEntity commentEntity = commentMapper.toEntity(dto);
            commentEntity.setAuthor(userEntity);
            commentEntity.setAd(adEntity);
            commentEntity.setText(dto.getText());
            commentEntity.setCreatedAt(LocalDateTime.now());
            return commentMapper.toCommentDto(commentRepository.save(commentEntity));}
        else {
            throw new NotFoundException("Ad is not found");}
    }

    @Override
    public Comment updateComment(Integer adPk,
                                    Integer commentId,
                                    CreateOrUpdateComment createOrUpdateCommentDto,
                                    Authentication authentication) throws NotFoundException{
        if (commentRepository.existsById(commentId)){
            CommentEntity commentEntity = getComment(commentId);
            commentEntity.setText(createOrUpdateCommentDto.getText());
            return commentMapper.toCommentDto(commentRepository.save(commentEntity));}
        else {
            throw new NotFoundException("Comment is not found");}
    }

    @Override
    public CommentEntity getComment(Integer pk) {
        return commentRepository.findById(pk).orElseThrow();
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId, Authentication authentication) throws NotFoundException {
        if (commentRepository.existsById(commentId)) {
            commentRepository.delete(getComment(commentId));
        } else {
            throw new NotFoundException("Comment is not found");
        }
    }
}
