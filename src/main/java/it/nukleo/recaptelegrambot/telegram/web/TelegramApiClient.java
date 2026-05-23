package it.nukleo.recaptelegrambot.telegram.web;

import it.nukleo.recaptelegrambot.config.TelegramBotProperties;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendMessageDto;
import it.nukleo.recaptelegrambot.telegram.dto.request.TelegramSendReactionDto;
import it.nukleo.recaptelegrambot.telegram.dto.response.TelegramReactionEmojiDto;
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


    public void sendMessage(Long chatId,  String text) {
        TelegramSendMessageDto dto = new TelegramSendMessageDto();
        dto.setChatId(chatId);
        dto.setText(text);

        telegramRestClient.post()
                .uri("/sendMessage")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }


    public void sendReaction(Long chatId, Long messageId, String emoji) {
        TelegramSendReactionDto dto = new TelegramSendReactionDto();
        TelegramReactionEmojiDto emojiDto = new TelegramReactionEmojiDto(emoji);
        dto.setChatId(chatId);
        dto.setMessageId(messageId);
        dto.setReaction(new TelegramReactionEmojiDto[]{emojiDto});

        telegramRestClient.post()
                .uri("/setMessageReaction")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }
}
