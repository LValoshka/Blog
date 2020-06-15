package com.example.blog.service.interfaces;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Status;

import java.util.List;

public interface ArticleService {

    List<Article> findAllByStatus(Status status) throws ResourceNotFoundException;

    Article save(Article article);

    void deleteArticle(String email, Article article);

    Article update(Article article, String email, Article newArticle);

    List<Article> getUsersArticles(String email);
}
