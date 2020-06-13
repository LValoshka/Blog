package com.example.blog.service.interfaces;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.User;

import java.util.Set;

public interface UserService {

    User findByUsername(String email);

    User save(User user);

    User findByActive(boolean active) throws ResourceNotFoundException;

    Set<Article> getUsersArticles();

    void auth(String email);

    void register(User user);

    void confirmEmail(String code) throws ResourceNotFoundException;
}
