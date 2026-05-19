package it.nukleo.recaptelegrambot.telegram.web;

import it.nukleo.recaptelegrambot.config.TelegramBotProperties;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendMessageDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TelegramApiClient {
    private final RestClient telegramRestClient;

    public TelegramApiClient(TelegramBotProperties properties) {
        this.telegramRestClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl() + "/bot" + properties.getToken())
                .build();
    }

    public void sendMessage(TelegramSendMessageDto request) {
        telegramRestClient.post()
                .uri("/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
