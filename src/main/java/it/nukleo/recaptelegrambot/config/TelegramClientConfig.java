package it.nukleo.recaptelegrambot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class TelegramClientConfig {
    private final TelegramBotProperties properties;

    @Bean
    public RestClient telegramRestClient() {
        return RestClient.builder()
                .baseUrl(properties.getBaseUrl() + "/bot" + properties.getToken())
                .build();
    }
}