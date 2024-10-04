package ru.skypro.homework.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.exception.NotFoundException;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.AdsServiceImpl;
import ru.skypro.homework.service.impl.CommentServiceImpl;

@CrossOrigin("http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class CommentController {

    private final UserRepository userRepository;
    private final CommentServiceImpl commentService;
    private final AdsServiceImpl adsService;


    @GetMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comments> getAllComments(@PathVariable Integer id, Authentication authentication){
        try {
            return ResponseEntity.ok(commentService.getComments(id, authentication));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Comment> addComment (@PathVariable("id") Integer id,
                                               @RequestBody CreateOrUpdateComment createOrUpdateComment,
                                               Authentication authentication){
        try {
            commentService.addComment(id, createOrUpdateComment, authentication);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(commentService.addComment(id, createOrUpdateComment, authentication));
    }
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