package com.example.blog.controller;

import com.example.blog.dto.ArticleDTO;
import com.example.blog.dto.ArticleUpdateDTO;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Status;
import com.example.blog.security.JwtTokenUtil;
import com.example.blog.service.interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ArticleController(ArticleService articleService, JwtTokenUtil jwtTokenUtil) {
        this.articleService = articleService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> getAllArticles() throws ResourceNotFoundException {
        return new ResponseEntity<>(articleService.findAllByStatus(Status.PUBLIC), HttpStatus.OK);
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> createArticle(@RequestBody ArticleDTO articleDTO) {
        Article article = articleDTO.createEntityFromDTO();
        return new ResponseEntity<>(articleService.save(article), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteArticleById(@PathVariable Article articleId,
                                                  @RequestHeader(AUTHORIZATION) String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        articleService.deleteArticle(email, articleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{articleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Article> updateArticle(@PathVariable Article articleId,
                                                 @RequestHeader(AUTHORIZATION) String token,
                                                 @Valid @RequestBody ArticleUpdateDTO articleUpdateDTO) {
        Article newArticle = articleUpdateDTO.createEntityFromDTO();
        String email = jwtTokenUtil.getUsernameFromToken(token);
        return new ResponseEntity<>(articleService.update(articleId, email, newArticle), HttpStatus.OK);
    }

    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Article>> getArticles(@RequestHeader(AUTHORIZATION) String token) {
        String email = jwtTokenUtil.getUsernameFromToken(token);
        return new ResponseEntity<>(articleService.getUsersArticles(email), HttpStatus.OK);
    }

}
