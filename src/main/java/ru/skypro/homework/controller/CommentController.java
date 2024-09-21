package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.utils.MethodLog;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "Комментарии")
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@RequestMapping("/ads")
public class CommentController {

    private final CommentService commentService;



    @Operation(
            tags = "Комментарии",
            summary = "Получение комментариев объявления"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comments.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<Comments> getComments(@PathVariable Integer id) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        List<Comment> commentList = commentService.getCommentsByAdId(id); // Переименовал переменную
        Comments comments = new Comments();
        comments.setResults(commentList);
        comments.setCount(commentList.size());
        return ResponseEntity.ok(comments);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Добавление комментария к объявлению"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable int id,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        Comment newComment = commentService.createComment(id, createOrUpdateComment);
        return ResponseEntity.ok(newComment);
    }

    @Operation(
            tags = "Комментарии",
            summary = "Удаление комментария"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable int adId, @PathVariable int commentId) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            tags = "Комментарии",
            summary = "Обновление комментария"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content)
    })
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable int adId, @PathVariable int commentId,
                                                 @RequestBody CreateOrUpdateComment createOrUpdateComment) {
        log.info("Использован метод {}", MethodLog.getMethodName());
        Comment updatedComment = commentService.updateComment(adId, commentId, createOrUpdateComment);
        return ResponseEntity.ok(updatedComment);
    }
}
