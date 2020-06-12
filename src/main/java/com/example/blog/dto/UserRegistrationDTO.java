package com.example.blog.dto;

import com.example.blog.model.Role;
import com.example.blog.model.User;
import lombok.Data;

import java.util.Collections;
import java.util.Date;

@Data
public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String password;
    private String email;

    public User convertDTOtoUser() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .username(email)
                .createdAt(new Date())
                .active(false)
                .roles(Collections.singleton(Role.ROLE_USER))
                .build();
    }
}
