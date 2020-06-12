package com.example.blog.util;

import com.example.blog.model.Article;
import com.example.blog.model.Comment;
import com.example.blog.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class JwtResponse {
    private String accessToken;

    private String tokenType = "Bearer";

    private int id;

    private String firstName;

    private String lastName;

    private String password;

    private String username;

    private Date createdAt;

    private boolean active;

    private Set<Role> roles;
}
