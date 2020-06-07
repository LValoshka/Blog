package com.example.blog.controller;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Status;
import com.example.blog.service.interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> getAllArticles() throws ResourceNotFoundException {
        return new ResponseEntity<>(articleService.findAllByStatus(Status.PUBLIC), HttpStatus.OK);
    }

    @PostMapping(value = "/articles", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {  //TO DO: replace article with ArticleDTO
        return new ResponseEntity<>(articleService.save(article), HttpStatus.OK);
    }

    @DeleteMapping(value = "/articles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteArticleById(@PathVariable String id) throws ResourceNotFoundException {
        articleService.deleteById(Integer.parseInt(id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/articles/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable String id, @Valid @RequestBody Article newArticle) //TO DO: replace article with ArticleDTO
            throws ResourceNotFoundException {
        return new ResponseEntity<>(articleService.update(Integer.parseInt(id), newArticle), HttpStatus.OK);
    }
}
