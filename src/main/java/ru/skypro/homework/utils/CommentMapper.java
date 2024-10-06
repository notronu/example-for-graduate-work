package ru.skypro.homework.utils;

import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentMapper {
    public CommentEntity toEntity(CreateOrUpdateComment createOrUpdateComment) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(createOrUpdateComment.getText());
        return commentEntity;
    }

    public Comment toCommentDto(CommentEntity commentEntity) {
        Comment comment = new Comment();
        comment.setPk(commentEntity.getId());
        comment.setCreatedAt(commentEntity.getCreatedAt());
        comment.setText(commentEntity.getText());
        UserEntity userEntity = commentEntity.getAuthor();
        comment.setAuthor(userEntity.getId());
        comment.setAuthorFirstName(userEntity.getFirstName());
        comment.setAuthorImage("/users/" + userEntity.getEmail() + "/image");
        return comment;
    }

    public Comments toCommentsDto(List<CommentEntity> comments) {
        Comments comments1 = new Comments();
        List<Comment> commentDtoList = comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());

        comments1.setCount(commentDtoList.size());
        comments1.setResults(commentDtoList);

        return comments1;
    }
}
