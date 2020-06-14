package com.example.blog.repository;

import com.example.blog.model.Article;
import com.example.blog.model.Status;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    Optional<List<Article>> findAllByStatus(Status status);

    Optional<List<Article>> findAllByAuthor(User user);

    Optional<List<Article>> findAllByTagSet(Set<Tag> tags);
}
