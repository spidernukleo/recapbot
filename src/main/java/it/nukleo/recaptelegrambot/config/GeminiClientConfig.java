package it.nukleo.recaptelegrambot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class GeminiClientConfig {
    private final GeminiApiProperties properties;

    @Bean
    public RestClient geminiRestClient() {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl() + "/" + properties.getModel())
                .defaultHeader("X-goog-api-key", properties.getKey())
                .build();
    }
}
