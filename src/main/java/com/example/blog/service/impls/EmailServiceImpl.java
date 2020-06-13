package com.example.blog.service.impls;

import com.example.blog.service.interfaces.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    @Value("${spring.mail.username}")
    private String senderEmail;

    private static final String URL_CONFIRM_REGISTRATION = "http://localhost:8080/auth/confirm/";

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMessageByEmail(String recipientEmail, String code) {
        log.info("---In sending email---");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(senderEmail);
        simpleMailMessage.setTo(recipientEmail);
        simpleMailMessage.setText("Confirm your registration by clicking this link: " + URL_CONFIRM_REGISTRATION + code);
        javaMailSender.send(simpleMailMessage);
    }
}
