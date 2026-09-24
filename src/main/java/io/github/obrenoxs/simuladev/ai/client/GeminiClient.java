package io.github.obrenoxs.simuladev.ai.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeminiClient {

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    private final RestClient restClient;

    public GeminiClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent")
                .build();
    }

    public GeminiResponse generateContent(GeminiRequest request) {
        return restClient.post()
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);
    }
}
