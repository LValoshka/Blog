package com.example.blog.service.impls;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Status;
import com.example.blog.model.User;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.interfaces.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Article> findAllByStatus(Status status) throws ResourceNotFoundException {
        return articleRepository.findAllByStatus(Status.PUBLIC).orElseThrow(() -> new ResourceNotFoundException("Article not found"));
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    @Override
    @Transactional
    public void deleteArticle(String email, Article article) {
        User user = userRepository.findByUsername(email).orElse(null);

        if (article.getAuthor().equals(user)) {
            articleRepository.delete(article);
        } else {
            log.error("No user with such id");
            //throw Exception?
        }

    }

    @Override
    @Transactional
    public Article update(Article article, String email, Article newArticle) {
        User user = userRepository.findByUsername(email).orElse(null);

        if (article.getAuthor().equals(user)) {
            article.setTitle(newArticle.getTitle());
            article.setText(newArticle.getText());
            article.setTagSet(newArticle.getTagSet());
            article.setStatus(newArticle.getStatus());
            return articleRepository.save(article);
        } else {
            log.error("No user with such id");
            return null;
        }
    }

    @Override
    public List<Article> getUsersArticles(String email) {
        User user = userRepository.findByUsername(email).orElse(null);
        return articleRepository.findAllByAuthor(user).orElse(null);
    }
}
