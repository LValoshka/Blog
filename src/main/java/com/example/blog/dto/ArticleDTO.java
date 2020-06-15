package com.example.blog.dto;

import com.example.blog.model.Article;
import com.example.blog.model.Status;
import com.example.blog.model.Tag;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class ArticleDTO {
    private String title;
    private String text;
    private Status status;
    private Set<Tag> tagSet = new HashSet<>();

    public Article createEntityFromDTO() {
        return Article.builder()
                .title(title)
                .text(text)
                .status(status)
                .updatedAt(new Date())
                .build();
    }
}
