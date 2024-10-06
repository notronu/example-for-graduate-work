package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.service.impl.CommentServiceImpl;

@CrossOrigin("http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
@Tag(name = "Комментарии", description = "Управление комментариями к объявлениям")
public class CommentController {

    private final CommentServiceImpl commentService;

    @Operation(summary = "Получить все комментарии", description = "Возвращает список всех комментариев к объявлению")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка комментариев"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content)
    })
    @GetMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comments> getAllComments(@PathVariable Integer id, Authentication authentication){
        try {
            return ResponseEntity.ok(commentService.getComments(id, authentication));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Добавить комментарий", description = "Добавляет новый комментарий к объявлению")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен"),
            @ApiResponse(responseCode = "404", description = "Объявление не найдено", content = @Content)
    })
    @PostMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment (@PathVariable("id") Integer id,
                                               @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                               Authentication authentication) {
        try {
            Comment comment = commentService.addComment(id, createOrUpdateComment, authentication);
            return ResponseEntity.ok(comment);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Удалить комментарий", description = "Удаляет комментарий по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Комментарий успешно удален"),
            @ApiResponse(responseCode = "404", description = "Комментарий или объявление не найдено", content = @Content)
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    @PreAuthorize("hasAuthority('ADMIN') or @commentServiceImpl.getComment(#commentId).user.email == authentication.principal.username")
    public ResponseEntity<?> deleteComment ( @PathVariable int adId, @PathVariable int commentId, Authentication
            authentication){
        try {
            commentService.deleteComment(adId, commentId, authentication);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Обновить комментарий", description = "Обновляет комментарий по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Комментарий или объявление не найдено", content = @Content)
    })
    @PatchMapping("{adId}/comments/{commentId}")
    @PreAuthorize("hasAuthority('ADMIN') or @commentServiceImpl.getComment(#commentId).user.email == authentication.principal.username")
    public ResponseEntity<Comment> updateComment (@PathVariable Integer adId, @PathVariable Integer commentId,
                                                     @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                                     Authentication authentication){
        try {
            commentService.updateComment(adId, commentId, createOrUpdateComment, authentication);
        } catch (NotFoundException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(commentService.updateComment(adId, commentId, createOrUpdateComment, authentication));
    }
}