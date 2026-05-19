package it.nukleo.recaptelegrambot.config;

import it.nukleo.recaptelegrambot.dto.request.TelegramSendMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TelegramApiClient {
    private final RestClient telegramRestClient;

    public void sendMessage(TelegramSendMessageDto request){
        telegramRestClient.post()
                .uri("/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}
