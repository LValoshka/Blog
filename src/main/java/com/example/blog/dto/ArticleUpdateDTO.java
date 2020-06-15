package com.example.blog.dto;

import com.example.blog.model.Article;
import com.example.blog.model.Status;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class ArticleUpdateDTO {
    private String title;

    private String text;

    private Status status;

    private Set<String> tags;

    public Article createEntityFromDTO(){
        return Article.builder()
                .title(title)
                .text(text)
                .status(status)
                .createdAt(new Date())
                .build();
    }
}
