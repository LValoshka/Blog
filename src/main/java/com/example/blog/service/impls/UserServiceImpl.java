package com.example.blog.service.impls;

import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.model.Article;
import com.example.blog.model.User;
import com.example.blog.repository.RedisRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.security.JwtTokenUtil;
import com.example.blog.service.interfaces.EmailService;
import com.example.blog.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final String REDIS_KEY_AUTH = "Auth";
    private static final String REDIS_KEY_RESET = "Reset";
    private static final String URL_CONFIRM_REGISTRATION = "http://localhost:8080/auth/confirm/";

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RedisRepository redisRepository;
    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                           JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService, RedisRepository redisRepository,
                           EmailService emailService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.redisRepository = redisRepository;
        this.emailService = emailService;
    }

    @Override
    public User findByUsername(String email) {
        return userRepository.findByUsername(email).orElse(null); //throw Exception! (Not found exception)
    }

    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User findByActive(boolean active) throws ResourceNotFoundException {
        return userRepository.findByActive(true).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Set<Article> getUsersArticles() {
        return getCurrentUser().getArticleSet();  //TO DO: rewrite!!!
    }


    @Override
    public void auth(String email) {
        User user = findByUsername(email);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        redisRepository.saveCode(REDIS_KEY_AUTH, user.getUsername(), token);
        emailService.sendMessageByEmail(user.getUsername(), URL_CONFIRM_REGISTRATION, token);
    }

    @Override
    public void register(User user) {
        save(user);
        auth(user.getUsername());
    }

    @Override
    public void confirmEmail(String code) throws ResourceNotFoundException {
        String email = (String) redisRepository.findAllCodes(REDIS_KEY_AUTH).entrySet()
                .stream()
                .filter(entry -> code.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User notActiveUser = userRepository.findByUsername(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        notActiveUser.setActive(true);
        userRepository.save(notActiveUser);
        redisRepository.deleteCode(REDIS_KEY_AUTH, email);
    }

    @Override
    public void sendEmailToRestorePassword(String email) {
        String code = UUID.randomUUID().toString();
        redisRepository.saveCode(REDIS_KEY_RESET, email, code);
        emailService.sendMessageByEmail(email, "", code);
    }

    @Override
    public void resetPassword(String code, String password) throws ResourceNotFoundException {
        String email = (String) redisRepository.findAllCodes(REDIS_KEY_RESET).entrySet()
                .stream()
                .filter(entry -> code.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User userWithOldPassword = userRepository.findByUsername(email).orElse(null);
        userWithOldPassword.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(userWithOldPassword);
        redisRepository.deleteCode(REDIS_KEY_RESET, email);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
