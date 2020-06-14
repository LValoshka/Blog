package com.example.blog.controller;

import com.example.blog.dto.UserAuthenticationDTO;
import com.example.blog.dto.UserEmailDto;
import com.example.blog.dto.UserPasswordDto;
import com.example.blog.dto.UserRegistrationDTO;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.User;
import com.example.blog.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/auth/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signUp(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        User user = userService.findByUsername(userRegistrationDTO.getEmail());
        if (user != null) {
            log.error("User with email " + userRegistrationDTO.getEmail() + " already exist");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        log.info("User with email " + userRegistrationDTO.getEmail() + " created");
        userService.register(userRegistrationDTO.createUserFromDTO());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value = "/auth/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserAuthenticationDTO userAuthenticationDTO) {
        User user = userService.findByUsername(userAuthenticationDTO.getEmail());
        if (user == null || user.isActive()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.auth(user.getUsername());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(value = "/auth/confirm/{code}")
    public ResponseEntity<?> confirmEmail(@PathVariable String code) throws ResourceNotFoundException {
        userService.confirmEmail(code);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserEmailDto userEmailDto) {
        userService.sendEmailToRestorePassword(userEmailDto.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/auth/reset")
    public ResponseEntity<?> resetPassword(@RequestBody UserPasswordDto userPasswordDto) throws ResourceNotFoundException {
        userService.resetPassword(userPasswordDto.getCode(), userPasswordDto.getPassword());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
