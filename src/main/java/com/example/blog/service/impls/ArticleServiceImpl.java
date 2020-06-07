package com.example.blog.service.impls;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.Status;
import com.example.blog.model.User;
import com.example.blog.repository.ArticleRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.interfaces.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
    public void deleteById(int id) throws ResourceNotFoundException {
        Article article = articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        if (article.getAuthor().getId() == getCurrentUser().getId()) {
            articleRepository.delete(article);
        } else {
            //throw Exception?
        }

    }

    @Override
    @Transactional
    public Article update(int id, Article newArticle) throws ResourceNotFoundException {   //rewrite???
        Article article = articleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        if (article.getAuthor().getId() == getCurrentUser().getId()) {
            article.setTitle(newArticle.getTitle());
            article.setText(newArticle.getText());
            article.setTagSet(newArticle.getTagSet());
            article.setStatus(newArticle.getStatus());
            return articleRepository.save(article);
        } else {
            return null;
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
