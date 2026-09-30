package io.github.obrenoxs.simuladev.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.mail-from}")
    private String mailFrom;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String token) {

        String link = baseUrl + "/api/v1/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject("Confirme seu e-mail");
        message.setText("Clique no link para confirmar: " + link);

        mailSender.send(message);
    }
}
