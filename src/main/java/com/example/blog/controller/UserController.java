package com.example.blog.controller;

import com.example.blog.dto.UserRegistrationDTO;
import com.example.blog.model.Article;
import com.example.blog.model.User;
import com.example.blog.security.JwtTokenUtil;
import com.example.blog.service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/auth/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> signUp(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        User user = userService.findByUsername(userRegistrationDTO.getEmail());
        if (user != null) {
            log.error("User with email " + userRegistrationDTO.getEmail() + " already exist");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        log.info("User with email " + userRegistrationDTO.getEmail() + " created");
        return new ResponseEntity<>(userService.save(userRegistrationDTO.convertDTOtoUser()), HttpStatus.OK);
    }

    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Article>> getArticles() {
        return new ResponseEntity<>(userService.getUsersArticles(), HttpStatus.OK);
    }

//    @PostMapping(value = "/auth/signIn", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = jwtTokenUtil.generateToken(authentication);
//
//        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer",
//                userDetails.getId(),
//                userDetails.getFirstName(),
//                userDetails.getLastName(),
//                userDetails.getPassword(),
//                userDetails.getUsername(),
//                userDetails.getCreatedAt(),
//                true,
//                (Set<Role>) userDetails.getAuthorities()));
//    }


}
