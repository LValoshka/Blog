package com.example.blog.service.interfaces;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Comment;

import java.util.List;

public interface CommentService {
    void saveComment(Comment comment, String email, Article articleId);

    List<Comment> findAllComments(Article articleId);

    Comment findComment(Article articleId, Integer commentId);

    void deleteComment(String email, Article articleId, Comment commentId) throws ResourceNotFoundException;

}

