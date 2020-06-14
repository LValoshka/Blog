package com.example.blog.dto;

import com.example.blog.model.Comment;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {
    private String message;

    public Comment createEntityFromDTO() {
        return Comment.builder()
                .message(message)
                .createdAt(new Date())
                .build();
    }
}
