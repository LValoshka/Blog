package com.example.blog.service.interfaces;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Status;

import java.util.List;

public interface ArticleService {

    List<Article> findAllByStatus(Status status) throws ResourceNotFoundException;

    Article save(Article article);

    void deleteById(int id) throws ResourceNotFoundException;

    Article update(int id, Article newArticle) throws ResourceNotFoundException;
}
