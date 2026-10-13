package io.github.obrenoxs.simuladev.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;

@Service
public class EmailService {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.mail-from}")
    private String mailFrom;

    private final String brevoApiKey;
    private final RestClient restClient;

    public EmailService(@Value("${app.brevo.api-key}") String brevoApiKey) {
        this.brevoApiKey = brevoApiKey;

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(10));

        this.restClient = RestClient.builder()
                .baseUrl("https://api.brevo.com")
                .requestFactory(factory)
                .build();
    }

    public void sendVerificationEmail(String to, String token) {
        String link = baseUrl + "/api/v1/auth/verify-email?token=" + token;

        BrevoEmailRequest body = new BrevoEmailRequest(
                new Person("SimulaDev", mailFrom),
                List.of(new Recipient(to)),
                "Confirme seu e-mail no SimulaDev",
                "Olá! Para ativar sua conta no SimulaDev, acesse o link abaixo:\n\n" + link
        );

        restClient.post()
                .uri("/v3/smtp/email")
                .header("api-key", brevoApiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

    private record Person(String name, String email) {}

    private record Recipient(String email) {}

    private record BrevoEmailRequest(Person sender, List<Recipient> to, String subject, String textContent) {}
}