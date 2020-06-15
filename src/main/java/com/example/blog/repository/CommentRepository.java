package com.example.blog.repository;

import com.example.blog.model.Article;
import com.example.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByPost(Article article);

    Optional<Comment> findByPostAndId(Article article, Integer id);
}
