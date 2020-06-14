package com.example.blog.controller;

import com.example.blog.dto.CommentDto;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Comment;
import com.example.blog.security.JwtTokenUtil;
import com.example.blog.service.interfaces.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/articles")
public class CommentController {
    private JwtTokenUtil jwtTokenUtil;
    private final CommentService commentService;

    public CommentController(JwtTokenUtil jwtTokenUtil, CommentService commentService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.commentService = commentService;
    }

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<?> addNewComment(@PathVariable Article articleId,
                                           @RequestHeader(AUTHORIZATION) String token,
                                           @Valid @RequestBody CommentDto commentDto) {
        Comment comment = commentDto.createEntityFromDTO();
        String email = jwtTokenUtil.getUsernameFromToken(token);
        commentService.saveComment(comment, email, articleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<Comment>> allCommentsFromArticle(@PathVariable Article articleId) {
        return new ResponseEntity<>(commentService.findAllComments(articleId), HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentFromArticle(@PathVariable Article articleId,
                                                         @PathVariable Integer commentId) {
        return new ResponseEntity<>(commentService.findComment(articleId, commentId), HttpStatus.OK);
    }

    @DeleteMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<?> deleteCommentFromArticle(@PathVariable Article articleId,
                                                      @RequestHeader(AUTHORIZATION) String token,
                                                      @PathVariable Comment commentId) throws ResourceNotFoundException {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        commentService.deleteComment(email, articleId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
